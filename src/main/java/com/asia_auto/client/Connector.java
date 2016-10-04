package com.asia_auto.client;


import com.asia_auto.client.gui.uicomponents.SettingsVeiwController;
import com.asia_auto.data.Connection;
import com.asia_auto.data.Message;

import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 29.08.2016.
 */
public class Connector extends Observable implements Runnable {
    private Logger logger = Logger.getLogger(Connector.class.getName());
    private Message message;

    public Connector(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try (Connection connection = new Connection(new Socket(SettingsVeiwController.getAdr(), Integer.parseInt(SettingsVeiwController.getPortt())))) {
            connection.send(message);
            message = connection.receive();
            setChanged();
            notifyObservers(message);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
        }
    }
}
