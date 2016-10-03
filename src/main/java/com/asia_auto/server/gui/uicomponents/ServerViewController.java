package com.asia_auto.server.gui.uicomponents;

import com.asia_auto.server.Server;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@FXMLController(value = "/resources/fxml/ui/ServerView.fxml")
public class ServerViewController {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXToggleButton startServer;
    private Logger logger = Logger.getLogger(ServerViewController.class.getName());

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "init");
        String homeDir = System.getProperty("user.home");
        final String settingsFilename = homeDir + File.separator + "server.properties";
        final Properties props = new Properties();
        try (FileInputStream inputStream = new FileInputStream(settingsFilename)) {
            props.load(inputStream);
        } catch (Exception e) {
        }
        if (!props.isEmpty()) {
            port.setText(props.getProperty("port"));
        }
        final Thread[] thread = {null};
        startServer.setOnAction(event -> {
            String portt = port.getText();
            props.setProperty("port", portt);
            try (FileOutputStream outputStream = new FileOutputStream(settingsFilename)) {
                props.store(outputStream, new Date().toString());
            } catch (Exception e) {
                logger.log(Level.INFO, e.toString());
            }
            if (startServer.isSelected()) {
                startServer.setSelected(true);
                thread[0] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Server server = new Server(Integer.parseInt(portt));
                        server.startServer(startServer);
                    }
                });
                thread[0].start();
            } else {
                if (thread[0] != null) {
                    startServer.setSelected(true);
                    startServer.setText("Выключение...");
                    thread[0].interrupt();
                } else {
                    startServer.setSelected(false);
                }
            }
        });
    }
}
