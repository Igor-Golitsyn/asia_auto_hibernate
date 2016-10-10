package com.asia_auto.client.gui.uicomponents;

import com.asia_auto.client.Connector;
import com.asia_auto.data.Message;
import com.asia_auto.data.MessageType;
import com.asia_auto.data.entity.*;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.Time;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@FXMLController(value = "/resources/fxml/ui/MainTableView.fxml")
public class MainTableViewController implements Observer {
    private Logger logger = Logger.getLogger(MainTableViewController.class.getName());
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<Person> treeTableView;
    @FXML
    private JFXTreeTableColumn<Person, String> timeColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> clientColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> masterColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> commentColumn;
    @FXML
    private Label treeTableViewCount;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXDatePicker targetDatePicker;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton removeButton;
    @FXML
    private JFXDialog addDialog;
    @FXML
    private JFXDialog chengeDialog;
    @FXML
    private JFXDialogLayout addDialogLayout;
    @FXML
    private JFXTextField family;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField secondName;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField town;
    @FXML
    private JFXTextField street;
    @FXML
    private JFXTextField house;
    @FXML
    private JFXTextField flat;
    @FXML
    private JFXTextField comment;
    @FXML
    private JFXComboBox<String> jfxTimesBox;
    @FXML
    private JFXButton addDialogAcceptButton;
    @FXML
    private VBox clientParametrs;
    @FXML
    private JFXTextField familyChengeDialog;
    @FXML
    private JFXTextField nameChengeDialog;
    @FXML
    private JFXTextField secondNameChengeDialog;
    @FXML
    private JFXTextField phoneChengeDialog;
    @FXML
    private JFXTextField emailChengeDialog;
    @FXML
    private JFXTextField townChengeDialog;
    @FXML
    private JFXTextField streetChengeDialog;
    @FXML
    private JFXTextField houseChengeDialog;
    @FXML
    private JFXTextField flatChengeDialog;
    @FXML
    private JFXTextField commentChengeDialog;
    @FXML
    private JFXButton chengeDialogAcceptButton;
    @FXML
    private JFXButton chengeDialogDelButton;
    @FXML
    private JFXDatePicker targetDatePickerChengeDialog;
    @FXML
    private JFXComboBox mastersComboBox;
    @FXML
    private JFXComboBox jfxTimesBoxchengeDialog;
    @FXML
    private JFXComboBox addDialogMastersComboBox;


    private Date date = new Date(new java.util.Date().getTime());
    private Message allTimes;
    private Message dataForDate;
    private Message allMasters;

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "init");
        targetDatePicker.setPadding(new Insets(0, 0, 20, 25));
        jfxTimesBox.setPadding(new Insets(0, 0, 25, 0));
        addDialogMastersComboBox.setPadding(new Insets(0, 0, 25, 0));
        runConnector(new Message(MessageType.GET_MAIN_FOR_DATE, date));
        runConnector(new Message(MessageType.GET_TIMES, null));
        datePicker.setValue(date.toLocalDate());
        datePicker.setOnAction((e) -> {
            logger.log(Level.INFO, "datePicker.setOnAction");
            date = Date.valueOf(datePicker.getValue());
            try {
                init();
            } catch (Exception e1) {
                logger.log(Level.WARNING, e1.getMessage());
            }
        });
        targetDatePicker.setOnAction((e) -> {
            logger.log(Level.INFO, "targetDatePicker.setOnAction");
            if (targetDatePicker.getValue() != null) {
                Date date = Date.valueOf(targetDatePicker.getValue());
                Message message = new Message(MessageType.GET_TIMES_FOR_DATE, date);
                runConnector(message);
            }
        });
        targetDatePickerChengeDialog.setOnAction(event -> {
            logger.log(Level.INFO, "targetDatePickerChengeDialog.setOnAction");
            if (targetDatePickerChengeDialog.getValue() != null) {
                Date date = Date.valueOf(targetDatePickerChengeDialog.getValue());
                Message message = new Message(MessageType.GET_TIMES_FOR_DATE, date);
                runConnector(message);
            }
        });
        addButton.setOnAction((event) -> {
            logger.log(Level.INFO, "addButton.setOnAction");
            if (allMasters == null) runConnector(new Message(MessageType.GET_MASTERS, null));
            name.setText("");
            secondName.setText("");
            family.setText("");
            phone.setText("");
            email.setText("");
            town.setText("");
            street.setText("");
            house.setText("");
            flat.setText("");
            comment.setText("");
            addDialogMastersComboBox.setValue("");
            targetDatePicker.setValue(null);
            ObservableList<String> strings = jfxTimesBox.getItems();
            strings.clear();
            jfxTimesBox.setItems(strings);
            addDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
        });
        removeButton.setOnAction((event) -> {
            logger.log(Level.INFO, "removeButton.setOnAction");
            if (allMasters == null) runConnector(new Message(MessageType.GET_MASTERS, null));
            TreeItem<Person> item = treeTableView.getFocusModel().getFocusedItem();
            if (item != null && item.getValue() != null) {
                Person person = item.getValue();
                Appointment appointment = (Appointment) dataForDate.getMap().get(person.id.getValue());
                nameChengeDialog.setText(appointment.getClient().getName());
                familyChengeDialog.setText(appointment.getClient().getFamily());
                secondNameChengeDialog.setText(appointment.getClient().getSecondName());
                commentChengeDialog.setText(appointment.getClient().getComment());
                emailChengeDialog.setText(appointment.getClient().getEmail());
                flatChengeDialog.setText(appointment.getClient().getFlat());
                houseChengeDialog.setText(appointment.getClient().getHouse());
                phoneChengeDialog.setText(appointment.getClient().getPhone());
                streetChengeDialog.setText(appointment.getClient().getStreet());
                townChengeDialog.setText(appointment.getClient().getTown());
                targetDatePickerChengeDialog.setValue(appointment.getDate().toLocalDate());
                String time = appointment.getTime().getTime().toString();
                time = time.substring(0, time.length() - 3);
                jfxTimesBoxchengeDialog.setValue(time);
                MasterElement master = appointment.getMaster();
                if (master != null) {
                    mastersComboBox.setValue(master.getFamily() + " " + master.getName() + " " + master.getSecondName());
                } else {
                    mastersComboBox.setValue("Мастер");
                }
                chengeDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
            }
        });
        chengeDialogAcceptButton.setOnAction(event -> {
            TreeItem<Person> item = treeTableView.getFocusModel().getFocusedItem();
            Person person = item.getValue();
            Appointment appointment = (Appointment) dataForDate.getMap().get(person.id.getValue());
            ClientElement client = appointment.getClient();
            client.setName(nameChengeDialog.getText());
            client.setSecondName(secondNameChengeDialog.getText());
            client.setFamily(familyChengeDialog.getText());
            client.setEmail(emailChengeDialog.getText());
            client.setComment(commentChengeDialog.getText());
            client.setFlat(flatChengeDialog.getText());
            client.setHouse(houseChengeDialog.getText());
            client.setStreet(streetChengeDialog.getText());
            client.setTown(townChengeDialog.getText());
            TreeMap<Long, Element> clientElementTreeMap = new TreeMap<>();
            clientElementTreeMap.put(client.getId(), client);
            Time time = Time.valueOf(jfxTimesBoxchengeDialog.getValue() + ":00");
            TimeElement timeElement = null;
            for (Element e : allTimes.getMap().values()) {
                TimeElement element = (TimeElement) e;
                if (element.getTime().equals(time)) timeElement = element;
            }
            String[] tempMasterParam = mastersComboBox.getValue().toString().split(" ");
            MasterElement tempMaster = new MasterElement(tempMasterParam[1], tempMasterParam[2], tempMasterParam[0], null, null);
            for (Element m : allMasters.getMap().values()) {
                MasterElement master = (MasterElement) m;
                if (master.equals(tempMaster)) tempMaster = master;
            }
            Date targetDate = Date.valueOf(targetDatePickerChengeDialog.getValue());
            appointment.setDate(targetDate);
            appointment.setMaster(tempMaster);
            appointment.setTime(timeElement);
            TreeMap<Long, Element> mainElementTreeMap = new TreeMap<>();
            mainElementTreeMap.put(appointment.getId(), appointment);
            Message updateMessage = new Message(MessageType.INPUT_DATA, mainElementTreeMap, targetDate, null);
            System.out.println(updateMessage);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chengeDialog.close();
                }
            });
            runConnector(updateMessage);
        });
        chengeDialogDelButton.setOnAction(event -> {
            logger.log(Level.INFO, "chengeDialogDelButton.setOnAction");
            TreeItem<Person> item = treeTableView.getFocusModel().getFocusedItem();
            Person person = item.getValue();
            TreeMap<Long, Element> mainElemMap = new TreeMap<>();
            Appointment appointment = (Appointment) dataForDate.getMap().get(person.id.getValue());
            mainElemMap.put(appointment.getId(), appointment);
            Message delMessage = new Message(MessageType.DELETE_DATA, mainElemMap, date, null);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chengeDialog.close();
                }
            });
            runConnector(delMessage);
        });

        addDialogAcceptButton.setOnAction((event) -> {
            logger.log(Level.INFO, "addDialogAcceptButton.setOnAction");
            Appointment appointment = new Appointment();
            appointment.setClient(new ClientElement(name.getText(), secondName.getText(), family.getText(), phone.getText(), email.getText(), town.getText(), street.getText(), house.getText(), flat.getText(), comment.getText()));
            Time time = Time.valueOf(jfxTimesBox.getValue() + ":00");
            TreeMap<Long, Element> timesMap = new TreeMap<>();
            for (Element e : allTimes.getMap().values()) {
                TimeElement element = (TimeElement) e;
                if (element.getTime().equals(time)) appointment.setTime(element);
            }
            String[] tempMasterParam = addDialogMastersComboBox.getValue().toString().split(" ");
            MasterElement tempMaster = new MasterElement(tempMasterParam[1], tempMasterParam[2], tempMasterParam[0], null, null);
            for (Element m : allMasters.getMap().values()) {
                MasterElement master = (MasterElement) m;
                if (master.equals(tempMaster)) appointment.setMaster(master);
            }
            appointment.setDate(Date.valueOf(targetDatePicker.getValue()));
            TreeMap<Long, Element> map = new TreeMap<Long, Element>();
            map.put(0L, appointment);
            Message registrationMess = new Message(MessageType.INPUT_DATA, map, null, null);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addDialog.close();
                }
            });
            runConnector(registrationMess);
        });
    }

    private void buildTable(Message messageDay) {
        logger.log(Level.INFO, "buildTable");
        timeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, String> param) -> {
            if (timeColumn.validateValue(param)) return param.getValue().getValue().time;
            else return timeColumn.getComputedValue(param);
        });
        clientColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, String> param) -> {
            if (clientColumn.validateValue(param)) return param.getValue().getValue().client;
            else return clientColumn.getComputedValue(param);
        });
        masterColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, String> param) -> {
            if (masterColumn.validateValue(param)) return param.getValue().getValue().master;
            else return masterColumn.getComputedValue(param);
        });
        commentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, String> param) -> {
            if (commentColumn.validateValue(param)) return param.getValue().getValue().comment;
            else return commentColumn.getComputedValue(param);
        });
        ObservableList<Person> people = FXCollections.observableArrayList();
        if (messageDay != null && messageDay.getMap() != null) {
            for (Element m : messageDay.getMap().values()) {
                Appointment appointment = (Appointment) m;
                String time = appointment.getTime().getTime().toString();
                time = time.substring(0, time.length() - 3);
                String clientFamily = appointment.getClient().getFamily();
                String clientName = appointment.getClient().getName();
                String clientSecondName = appointment.getClient().getSecondName();
                String clientPhone = appointment.getClient().getPhone();
                String clientComment = appointment.getClient().getComment();
                long id = appointment.getId();
                String master = appointment.getMaster() == null ? "" : appointment.getMaster().getFamily();
                Person person = new Person(id, time, clientFamily + " " + clientName + " " + clientSecondName + " " + clientPhone, master, clientComment);
                people.add(person);
            }
        }
        treeTableView.setRoot(new RecursiveTreeItem<Person>(people, RecursiveTreeObject::getChildren));
        treeTableView.setShowRoot(false);
        treeTableViewCount.textProperty().bind(Bindings.createStringBinding(() -> "( " + treeTableView.getCurrentItemsCount() + " )", treeTableView.currentItemsCountProperty()));
        searchField.textProperty().addListener((o, oldVal, newVal) -> {
            treeTableView.setPredicate(person -> person.getValue().time.get().contains(newVal) || person.getValue().client.get().contains(newVal) || (person.getValue().master.get() + "").contains(newVal));
        });
    }

    /**
     * Заполняет свободным временем комбо бокс
     *
     * @return
     */
    private ObservableList<String> fullingTimesBox(TreeMap<Long, Element> map, JFXComboBox box) {
        logger.log(Level.INFO, "fullingTimesBox " + map + box.getId());
        ObservableList<String> strings = box.getItems();
        strings.clear();
        for (Element e : map.values()) {
            TimeElement timeElement = (TimeElement) e;
            String time = timeElement.getTime().toString();
            time = time.substring(0, time.length() - 3);
            strings.add(time);
        }
        return strings;
    }

    private ObservableList<String> fullingMastersBox(TreeMap<Long, Element> map, JFXComboBox box) {
        logger.log(Level.INFO, "fullingMastersBox " + map + box.getId());
        ObservableList<String> strings = box.getItems();
        strings.clear();
        for (Element m : map.values()) {
            MasterElement masterElement = (MasterElement) m;
            String master = masterElement.getFamily() + " " + masterElement.getName() + " " + masterElement.getSecondName();
            strings.add(master);
        }
        return strings;
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.log(Level.INFO, "update " + ((Message) arg).getType());
        Message message = (Message) arg;
        switch (message.getType()) {
            case MAIN_FOR_DATE:
                if (message.getDate().toString().equals(date.toString())) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            dataForDate = message;
                            buildTable(message);
                            treeTableView.refresh();
                        }
                    });
                }
                break;
            case TIMES:
                allTimes = message;
                break;
            case TIMES_FOR_DATE:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fullingTimesBox(message.getMap(), jfxTimesBox);
                        fullingTimesBox(message.getMap(), jfxTimesBoxchengeDialog);
                    }
                });
                break;
            case MASTERS:
                allMasters = message;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fullingMastersBox(message.getMap(), mastersComboBox);
                        fullingMastersBox(message.getMap(), addDialogMastersComboBox);
                    }
                });
                break;
            case ACCEPTED:
                runConnector(new Message(MessageType.GET_MAIN_FOR_DATE, date));
                break;
        }
    }

    private void runConnector(Message message) {
        logger.log(Level.INFO, "runConnector " + message.getType());
        Connector connector = new Connector(message);
        connector.addObserver(this);
        new Thread(connector).start();
    }

    /*
     * data class
     */
    class Person extends RecursiveTreeObject<Person> {
        LongProperty id;
        StringProperty time;
        StringProperty client;
        StringProperty master;
        StringProperty comment;

        public Person(long id, String time, String client, String master, String comment) {
            this.id = new SimpleLongProperty(id);
            this.time = new SimpleStringProperty(time);
            this.client = new SimpleStringProperty(client);
            this.master = new SimpleStringProperty(master);
            this.comment = new SimpleStringProperty(comment);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", time=" + time +
                    ", client=" + client +
                    ", master=" + master +
                    ", comment=" + comment +
                    '}';
        }
    }
}
