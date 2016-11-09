package com.asia_auto.client.gui.uicomponents;


import com.asia_auto.client.Connector;
import com.asia_auto.client.Model;
import com.asia_auto.client.components.imageUtil.Image;
import com.asia_auto.client.components.imageUtil.ImageLoader;
import com.asia_auto.data.Message;
import com.asia_auto.data.MessageType;
import com.asia_auto.data.entity.Element;
import com.asia_auto.data.entity.MasterElement;
import com.jfoenix.controls.*;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 01.09.2016.
 */
@FXMLController(value = "/resources/fxml/ui/MastersView.fxml")
public class MastersVeiwController implements Observer {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private HBox hBox;
    @FXML
    private StackPane root;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton fotoButton;
    @FXML
    private JFXButton addDialogAcceptButton;
    @FXML
    private JFXTextField family;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField secondName;
    @FXML
    private JFXTextField smena;
    @FXML
    private JFXTextField fotoText;
    @FXML
    private JFXDialog addMasterDialog;
    @FXML
    private JFXDialog removeDialog;
    @FXML
    private JFXDialogLayout removeDialogLayout;
    @FXML
    private JFXTextField familyMasterRemoveDialog;
    @FXML
    private JFXTextField nameMasterRemoveDialog;
    @FXML
    private JFXTextField secondNameMasterRemoveDialog;
    @FXML
    private JFXTextField smenaMasterRemoveDialog;
    @FXML
    private JFXTextField fotoTextMasterRemoveDialog;
    @FXML
    private JFXTextField idMaster;
    @FXML
    private JFXButton fotoButtonMasterRemoveDialog;
    @FXML
    private JFXButton removeDialogAcceptButton;
    @FXML
    private JFXButton removeDialogDelButton;

    private Logger logger = Logger.getLogger(MastersVeiwController.class.getName());
    private Date date = new Date(new java.util.Date().getTime());
    private byte[] imageMasterRemoveDialog = null;

    @Override
    public synchronized void update(Observable o, Object arg) {
        logger.log(Level.INFO, "update");
        Message message = (Message) arg;
        switch (message.getType()) {
            case ACCEPTED:
                runModel(new Message(MessageType.GET_MASTERS, null));
                break;
            case MASTERS:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        createTables(message);
                    }
                });
                break;
            default:
                runModel(new Message(MessageType.GET_MASTERS, null));
                break;
        }
    }

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "init");
        runModel(new Message(MessageType.GET_MASTERS, null));
        addButton.setOnAction(event -> {
            addButton();
        });
        addDialogAcceptButton.setOnAction(event -> {
            addDialogAcceptButton();
        });
        fotoButton.setOnAction(event -> {
            fotoButton();
        });
        fotoButtonMasterRemoveDialog.setOnAction(event -> {
            fotoButtonMasterRemoveDialog();
        });
        removeDialogAcceptButton.setOnAction(event -> {
            removeDialogAcceptButton();
        });
        removeDialogDelButton.setOnAction(event -> {
            removeDialogDelButton();
        });
    }

    private void addButton() {
        logger.log(Level.INFO, "addButton");
        family.setText("");
        name.setText("");
        secondName.setText("");
        smena.setText("");
        fotoText.setText("");
        addMasterDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
    }

    private void addDialogAcceptButton() {
        logger.log(Level.INFO, "addDialogAcceptButton");
        String famil = family.getText();
        String nam = name.getText();
        String secName = secondName.getText();
        String smen = smena.getText();
        byte[] fot = new byte[0];
        fot = imageLoad(fotoText.getText());
        MasterElement master = new MasterElement(nam, secName, famil, smen, fot);
        TreeMap<Long, Element> map = new TreeMap<>();
        map.put(master.getId(), master);
        Message message = new Message(MessageType.INPUT_DATA, map, date, null);
        runModel(message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addMasterDialog.close();
            }
        });
    }

    private void fotoButton() {
        logger.log(Level.INFO, "fotoButton");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG & PNG файлы", "*.jpg", "*.jpeg", "*.png"));
        fileChooser.setTitle("Выберите фото мастера:");
        String file = fileChooser.showOpenDialog(root.getScene().getWindow()).toString();
        fotoText.setText(file);
    }

    private void fotoButtonMasterRemoveDialog() {
        logger.log(Level.INFO, "fotoButtonMasterRemoveDialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG & PNG файлы", "*.jpg", "*.jpeg", "*.png"));
        fileChooser.setTitle("Выберите фото мастера:");
        String file = fileChooser.showOpenDialog(root.getScene().getWindow()).toString();
        fotoTextMasterRemoveDialog.setText(file);
    }

    private void removeDialogAcceptButton() {
        logger.log(Level.INFO, "removeDialogAcceptButton");
        long id = Integer.parseInt(idMaster.getText());
        String name = nameMasterRemoveDialog.getText();
        String secondName = secondNameMasterRemoveDialog.getText();
        String family = familyMasterRemoveDialog.getText();
        String smena = smenaMasterRemoveDialog.getText();
        if (fotoTextMasterRemoveDialog.getText() == null || fotoTextMasterRemoveDialog.getText().isEmpty()) {
            if (imageMasterRemoveDialog == null) {
                imageMasterRemoveDialog = imageLoad(null);
            }
        } else {
            imageMasterRemoveDialog = imageLoad(fotoTextMasterRemoveDialog.getText());
        }
        MasterElement master = new MasterElement(name, secondName, family, smena, imageMasterRemoveDialog);
        master.setId(id);
        TreeMap<Long, Element> mastersMap = new TreeMap<>();
        mastersMap.put(master.getId(), master);
        runModel(new Message(MessageType.INPUT_DATA, mastersMap, date, null));
        imageMasterRemoveDialog = null;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                removeDialog.close();
            }
        });
    }

    private void removeDialogDelButton() {
        logger.log(Level.INFO, "removeDialogDelButton");
        long id = Integer.parseInt(idMaster.getText());
        String name = nameMasterRemoveDialog.getText();
        String secondName = secondNameMasterRemoveDialog.getText();
        String family = familyMasterRemoveDialog.getText();
        String smena = smenaMasterRemoveDialog.getText();
        if (fotoTextMasterRemoveDialog.getText() == null || fotoTextMasterRemoveDialog.getText().isEmpty()) {
            if (imageMasterRemoveDialog == null) {
                imageMasterRemoveDialog = imageLoad(null);
            }
        } else {
            imageMasterRemoveDialog = imageLoad(fotoTextMasterRemoveDialog.getText());
        }
        MasterElement master = new MasterElement(name, secondName, family, smena, imageMasterRemoveDialog);
        master.setId(id);
        TreeMap<Long, Element> mastersMap = new TreeMap<>();
        mastersMap.put(master.getId(), master);
        runModel(new Message(MessageType.DELETE_DATA, mastersMap, date, null));
        imageMasterRemoveDialog = null;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                removeDialog.close();
            }
        });
    }

    /**
     * Загружает картинку с диска, в случае ошибки грузит свою.
     *
     * @param text
     * @return
     */
    private byte[] imageLoad(String text) {
        logger.log(Level.INFO, "imageLoad");
        byte[] image = null;
        File file = new File("src\\main\\resources\\resources\\1.jpg");
        try {
            System.out.println(file.getCanonicalPath() + " " + file.isFile());
            if (text == null || text.isEmpty()) text = file.getCanonicalPath();
            image = Files.readAllBytes(Paths.get(text));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        if (image == null) try {
            image = Files.readAllBytes(file.getCanonicalFile().toPath());
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return image;
    }

    private void runModel(Message message) {
        logger.log(Level.INFO, "runModel " + message.getType());
        Model model=new Model(message);
        model.addObserver(this);
        new Thread(model).start();
    }

    /**
     * Создает списки смен с мастерами
     *
     * @param message
     */
    private void createTables(Message message) {
        logger.log(Level.INFO, "createTables " + message);
        hBox.getChildren().clear();
        Set<String> smens = new HashSet<>();
        Collection<Element> masters = message.getMap().values();
        for (Element m : masters) {
            MasterElement master = (MasterElement) m;
            smens.add(master.getSmena());
        }
        for (String smena : smens) {
            JFXListView mainL = new JFXListView<>();
            mainL.setPrefWidth(300);
            mainL.setMinWidth(300);
            mainL.maxWidth(300);
            mainL.depthProperty().setValue(1);
            mainL.getItems().add(createMasters(smena, masters));
            hBox.getChildren().add(mainL);
        }

    }

    /**
     * Создает список мастеров на смену
     *
     * @param smena
     * @param masters
     * @return
     */
    private JFXListView createMasters(String smena, Collection<Element> masters) {
        logger.log(Level.INFO, "createMasters " + masters);
        anchorPane.setMaxHeight(root.getHeight());
        anchorPane.setMinHeight(root.getHeight());
        anchorPane.setPrefHeight(root.getHeight());
        JFXListView listView = new JFXListView();
        listView.setMaxHeight(root.getHeight() - 150);
        listView.setPrefWidth(250);
        listView.minWidth(250);
        listView.maxWidth(250);
        listView.setGroupnode(new Label("Смена " + smena));
        ObservableList items = listView.getItems();
        for (Element m : masters) {
            MasterElement master = (MasterElement) m;
            if (master.getSmena().equals(smena)) {
                Image imageUtil = null;
                try {
                    imageUtil = ImageLoader.fromBytes(master.getFoto());
                } catch (IOException e) {
                    logger.log(Level.WARNING, e.toString());
                }
                imageUtil = imageUtil.getResizedToSquare(100, 0);
                ImageView imageView = new ImageView();
                imageView.setImage(SwingFXUtils.toFXImage(imageUtil.getBufferedImage(), null));
                String name = master.getFamily() + "\n" + master.getName() + "\n" + master.getSecondName();
                Label label = new Label(name, imageView);
                label.setOnMouseClicked(event -> {
                    logger.log(Level.INFO, "label.setOnMouseClicked");
                    idMaster.setText(String.valueOf(master.getId()));
                    nameMasterRemoveDialog.setText(master.getName());
                    familyMasterRemoveDialog.setText(master.getFamily());
                    secondNameMasterRemoveDialog.setText(master.getSecondName());
                    smenaMasterRemoveDialog.setText(master.getSmena());
                    fotoTextMasterRemoveDialog.setText("");
                    imageMasterRemoveDialog = master.getFoto();
                    removeDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
                });
                items.add(label);
            }
        }
        listView.setItems(items);
        listView.depthProperty().setValue(1);
        return listView;
    }
}
