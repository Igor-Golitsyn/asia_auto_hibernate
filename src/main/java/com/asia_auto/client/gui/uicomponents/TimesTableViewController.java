package com.asia_auto.client.gui.uicomponents;

import com.asia_auto.client.Connector;
import com.asia_auto.data.Message;
import com.asia_auto.data.MessageType;
import com.asia_auto.data.entity.Element;
import com.asia_auto.data.entity.TimeElement;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 04.10.2016.
 */
@FXMLController(value = "/resources/fxml/ui/TimeTableView.fxml")
public class TimesTableViewController implements Observer {
    private Logger logger = Logger.getLogger(MainTableViewController.class.getName());
    private Message times = null;
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<Person> treeTableView;
    @FXML
    private JFXTreeTableColumn<Person, String> timeColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> commentColumn;
    @FXML
    private Label treeTableViewCount;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXDialog addDialog;
    @FXML
    private JFXDialog chengeDialog;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton editButton;
    @FXML
    private JFXTextField comment;
    @FXML
    private JFXTextField commentEdit;
    @FXML
    private JFXButton addDialogAcceptButton;
    @FXML
    private JFXButton chengeDialogAcceptButton;
    @FXML
    private JFXButton chengeDialogDelButton;
    @FXML
    private JFXDatePicker targetTimePicker;
    @FXML
    private JFXDatePicker targetTimePickerChengeDialog;

    @PostConstruct
    public void init() {
        targetTimePicker.setPadding(new Insets(0, 0, 20, 25));
        targetTimePickerChengeDialog.setPadding(new Insets(0, 0, 20, 0));
        if (times == null) runConnector(new Message(MessageType.GET_TIMES, null));
        else buildTable(times);
        addButton.setOnAction(event -> {
            comment.setText("");
            addDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
        });
        editButton.setOnAction(event -> {
            TreeItem<Person> item = treeTableView.getFocusModel().getFocusedItem();
            Person person = item.getValue();
            TimeElement timeElement = (TimeElement) times.getMap().get(person.id.getValue());
            commentEdit.setText(timeElement.getComment());
            targetTimePickerChengeDialog.setTime(timeElement.getTime().toLocalTime());
            chengeDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
        });
        addDialogAcceptButton.setOnAction(event -> {
            TimeElement timeElement = new TimeElement();
            Time time = new Time(targetTimePicker.getTime().getHour(), targetTimePicker.getTime().getMinute(), 0);
            timeElement.setTime(time);
            timeElement.setComment(comment.getText());
            for (Element e : times.getMap().values()) {
                TimeElement timeEl = (TimeElement) e;
                if (timeEl.equals(timeElement)) {
                    timeElement = timeEl;
                }
            }
            if (timeElement.getId()==0) {
                TreeMap<Long, Element> map = new TreeMap<>();
                map.put(0L, timeElement);
                Message message = new Message(MessageType.INPUT_DATA, map, null, null);
                runConnector(message);
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addDialog.close();
                }
            });
        });
        chengeDialogAcceptButton.setOnAction(event -> {
            TreeItem<Person> item = treeTableView.getFocusModel().getFocusedItem();
            Person person = item.getValue();
            TimeElement timeElement = (TimeElement) times.getMap().get(person.id.getValue());
            Time time = new Time(targetTimePickerChengeDialog.getTime().getHour(), targetTimePickerChengeDialog.getTime().getMinute(), 0);
            timeElement.setComment(commentEdit.getText());
            timeElement.setTime(time);
            TreeMap<Long, Element> map = new TreeMap<>();
            map.put(0L, timeElement);
            Message message = new Message(MessageType.INPUT_DATA, map, null, null);
            runConnector(message);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chengeDialog.close();
                }
            });
        });
        chengeDialogDelButton.setOnAction(event -> {
            TreeItem<Person> item = treeTableView.getFocusModel().getFocusedItem();
            Person person = item.getValue();
            TimeElement timeElement = (TimeElement) times.getMap().get(person.id.getValue());
            TreeMap<Long, Element> map = new TreeMap<>();
            map.put(timeElement.getId(), timeElement);
            Message message = new Message(MessageType.DELETE_DATA, map, null, null);
            runConnector(message);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chengeDialog.close();
                }
            });
        });
    }

    private void buildTable(Message messageDay) {
        logger.log(Level.INFO, "buildTable");
        timeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, String> param) -> {
            if (timeColumn.validateValue(param)) return param.getValue().getValue().time;
            else return timeColumn.getComputedValue(param);
        });
        commentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, String> param) -> {
            if (commentColumn.validateValue(param)) return param.getValue().getValue().comment;
            else return commentColumn.getComputedValue(param);
        });
        ObservableList<Person> people = FXCollections.observableArrayList();
        if (messageDay != null && messageDay.getMap() != null) {
            for (Element m : messageDay.getMap().values()) {
                TimeElement timeElement = (TimeElement) m;
                String time = timeElement.getTime().toString();
                time = time.substring(0, time.length() - 3);
                String comment = timeElement.getComment();
                long id = timeElement.getId();
                Person person = new Person(id, time, comment);
                people.add(person);
            }
        }
        treeTableView.setRoot(new RecursiveTreeItem<Person>(people, RecursiveTreeObject::getChildren));
        treeTableView.setShowRoot(false);
        treeTableViewCount.textProperty().bind(Bindings.createStringBinding(() -> "( " + treeTableView.getCurrentItemsCount() + " )", treeTableView.currentItemsCountProperty()));
        searchField.textProperty().addListener((o, oldVal, newVal) -> {
            treeTableView.setPredicate(person -> person.getValue().time.get().contains(newVal) || (person.getValue().comment.get() + "").contains(newVal));
        });
    }

    private void runConnector(Message message) {
        logger.log(Level.INFO, "runConnector " + message.getType());
        Connector connector = new Connector(message);
        connector.addObserver(this);
        new Thread(connector).start();
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.log(Level.INFO, "update " + ((Message) arg).getType());
        Message mess = (Message) arg;
        switch (mess.getType()) {
            case TIMES:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        times = (Message) arg;
                        buildTable(times);
                        treeTableView.refresh();
                    }
                });
                break;
            default:
                runConnector(new Message(MessageType.GET_TIMES, null));
                break;
        }
    }

    class Person extends RecursiveTreeObject<Person> {
        LongProperty id;
        StringProperty time;
        StringProperty comment;

        public Person(long id, String time, String comment) {
            this.id = new SimpleLongProperty(id);
            this.time = new SimpleStringProperty(time);
            this.comment = new SimpleStringProperty(comment);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", time=" + time +
                    ", comment=" + comment +
                    '}';
        }
    }
}
