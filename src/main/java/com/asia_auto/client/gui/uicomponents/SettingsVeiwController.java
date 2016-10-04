package com.asia_auto.client.gui.uicomponents;

import com.asia_auto.data.Connection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 15.09.2016.
 */
@FXMLController(value = "/resources/fxml/ui/SettingsView.fxml")
public class SettingsVeiwController {
    private Logger logger = Logger.getLogger(SettingsVeiwController.class.getName());
    @FXML
    private JFXTextField adress;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXButton testButton;
    private static String adr;
    private static String portt;

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "update");
        String homeDir = System.getProperty("user.home");
        final String settingsFilename = homeDir + File.separator + "client.properties";
        final Properties props = new Properties();
        try (FileInputStream inputStream = new FileInputStream(settingsFilename)) {
            props.load(inputStream);
        } catch (Exception e) {
        }
        if (!props.isEmpty()) {
            adress.setText(props.getProperty("adress"));
            port.setText(props.getProperty("portt"));
        }
        testButton.setOnAction(event -> {
            adr = adress.getText();
            portt = port.getText();
            try (Connection connection = new Connection(new Socket(adr, Integer.parseInt(portt)))) {
                props.setProperty("adress", adr);
                props.setProperty("portt", portt);
                testButton.setText("Соединение установлено");
                try (FileOutputStream outputStream = new FileOutputStream(settingsFilename)) {
                    props.store(outputStream, new Date().toString());
                } catch (FileNotFoundException e) {
                    logger.log(Level.WARNING, e.getMessage());
                }
            } catch (Exception e) {
                testButton.setText("Ошибка соединения");
            }
        });
    }

    public static String getAdr() {
        return adr;
    }

    public static void setAdr(String adr) {
        SettingsVeiwController.adr = adr;
    }

    public static String getPortt() {
        return portt;
    }

    public static void setPortt(String portt) {
        SettingsVeiwController.portt = portt;
    }
}
