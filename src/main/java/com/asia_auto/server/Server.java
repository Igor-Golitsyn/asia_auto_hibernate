package com.asia_auto.server;

import com.asia_auto.data.Connection;
import com.asia_auto.data.Message;
import com.asia_auto.data.MessageType;
import com.asia_auto.data.dao.ElementEntityDao;
import com.asia_auto.data.entity.*;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 08.09.2016.
 */
public class Server {
    private int port;
    private Logger logger = Logger.getLogger(Server.class.getName());

    public Server(int port) {
        logger.log(Level.INFO, "Server");
        this.port = port;
    }

    public void startServer(JFXToggleButton startServer) {
        logger.log(Level.INFO, "startServer");
        startServer.setSelected(true);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(1000);
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket;
                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                }
                new Handler(socket).start();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                startServer.setSelected(false);
                startServer.setText("ВКЛ/ВЫКЛ");
            }
        });
    }

    public void startServer() {
        logger.log(Level.INFO, "startServer");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Класс работает с новым подключением.
     */
    private class Handler extends Thread {
        private Socket socket;
        private ElementEntityDao entityDao = ElementEntityDao.getInstance();

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (Connection connection = new Connection(socket)) {
                while (true) {
                    Message message = connection.receive();
                    switch (message.getType()) {
                        case GET_CLIENTS:
                            connection.send(createMessageFromTable(ClientElement.class, MessageType.CLIENTS));
                            break;
                        case GET_MASTERS:
                            connection.send(createMessageFromTable(MasterElement.class, MessageType.MASTERS));
                            break;
                        case GET_TIMES:
                            connection.send(createMessageFromTable(TimeElement.class, MessageType.TIMES));
                            break;
                        case GET_MAIN_FOR_DATE:
                            connection.send(createMessageForDate(MessageType.MAIN_FOR_DATE, message.getDate()));
                            break;
                        case GET_TIMES_FOR_DATE:
                            connection.send(createMessageForDate(MessageType.TIMES_FOR_DATE, message.getDate()));
                            break;
                        case INPUT_DATA:
                            connection.send(createMessageFromInputData(message));
                            break;
                        case DELETE_DATA:
                            connection.send(createMesageFromDel(message));
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.WARNING, e.toString());
            }
        }

        private Message createMesageFromDel(Message message) {
            MessageType type = MessageType.ACCEPTED;
            for (Element e : message.getMap().values()) {
                if (!entityDao.deleteElement(e.getClass(), e.getId())) {
                    type = MessageType.ERROR;
                    break;
                }
            }
            return new Message(type, null, message.getDate(), null);
        }

        private Message createMessageFromInputData(Message message) {
            MessageType type = MessageType.ACCEPTED;
            TreeMap<Long, Element> map = new TreeMap<>();
            for (Element e : message.getMap().values()) {
                Element nElem = entityDao.writeElement(e);
                if (nElem.equals(e)) {
                    map.put(nElem.getId(), nElem);
                } else {
                    type = MessageType.ERROR;
                    map = null;
                    break;
                }
            }
            return new Message(type, map, message.getDate(), null);
        }

        private <T extends Element> Message createMessageFromTable(Class<T> clazz, MessageType forResponse) {
            TreeMap<Long, Element> map = new TreeMap<>();
            List<T> list = entityDao.getAllElements(clazz);
            for (T t : list) {
                map.put(t.getId(), t);
            }
            return new Message(forResponse, map, null, null);
        }

        private Message createMessageForDate(MessageType forResponse, Date date) {
            TreeMap<Long, Element> map = new TreeMap<>();
            List<MainElement> mainList = entityDao.getMainForDate(date);
            List<TimeElement> timeList = entityDao.getAllElements(TimeElement.class);
            switch (forResponse) {
                case MAIN_FOR_DATE:
                    for (MainElement mainElement : mainList) {
                        map.put(mainElement.getId(), mainElement);
                    }
                    break;
                case TIMES_FOR_DATE:  // лимит одновременных клиентов
                    for (TimeElement timeElement : timeList) {
                        int count = 0;
                        if (mainList != null && mainList.size() != 0) {
                            for (MainElement mainElement : mainList) {
                                if (timeElement.equals(mainElement.getTime())) count++;
                            }
                        }
                        if (count < 2) map.put(timeElement.getId(), timeElement);
                    }
            }
            return new Message(forResponse, map, date, null);
        }
    }
}
