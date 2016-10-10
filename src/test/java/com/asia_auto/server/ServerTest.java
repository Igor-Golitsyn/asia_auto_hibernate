package com.asia_auto.server;

import com.asia_auto.data.Connection;
import com.asia_auto.data.Message;
import com.asia_auto.data.MessageType;
import com.asia_auto.data.entity.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Игорь on 03.10.2016.
 */
public class ServerTest {
    private CopyOnWriteArraySet<Element> set = new CopyOnWriteArraySet<>();
    private TimeElement timeElement;
    private Date date = new Date(0);

    @BeforeTest
    public void startServer() {
        timeElement = new TimeElement(new Time(getRandomInt(), getRandomInt(), getRandomInt()));
        TreeMap<Long, Element> map = new TreeMap<>();
        map.put(0L, timeElement);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Server(81).startServer();
            }
        }).start();
        try (Connection connection = new Connection(new Socket("127.0.0.1", 81))) {
            connection.send(new Message(MessageType.INPUT_DATA, map, null, null));
            Message rezult = connection.receive();
            if (rezult.getType() == MessageType.ACCEPTED) {
                connection.send(new Message(MessageType.GET_TIMES, null));
                map = connection.receive().getMap();
                for (Element e : map.values()) {
                    timeElement = (TimeElement) e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(threadPoolSize = 100, invocationCount = 100, priority = 1)
    public void inputData() {
        TreeMap<Long, Element> map = new TreeMap<>();
        MasterElement masterElement = new MasterElement(getRandomString(), getRandomString(), getRandomString(), "1", null);
        ClientElement clientElement = new ClientElement(getRandomString(), getRandomString(), getRandomString(), null, null, null, null, null, null, null);
        Appointment appointment = new Appointment(timeElement, clientElement, masterElement, date);
        map.put(0L, appointment);
        Message message = new Message(MessageType.INPUT_DATA, map, date, null);
        try (Connection connection = new Connection(new Socket("127.0.0.1", 81))) {
            connection.send(message);
            map = connection.receive().getMap();
            Appointment nMain = null;
            for (Element e : map.values()) {
                nMain = (Appointment) e;
            }
            set.add(nMain);
            Assert.assertTrue(nMain.equals(appointment));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 2)
    public void clear() {
        TreeMap<Long, Element> map = new TreeMap<>();
        for (Element e : set) {
            map.put(e.getId(), e);
        }
        Message message = new Message(MessageType.DELETE_DATA, map, null, null);
        try (Connection connection = new Connection(new Socket("127.0.0.1", 81))) {
            connection.send(message);
            message = connection.receive();
            Assert.assertTrue(MessageType.ACCEPTED == message.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getRandomInt() {
        return ThreadLocalRandom.current().nextInt(60);
    }

    private String getRandomString() {
        StringBuffer sb = new StringBuffer();
        for (int x = 0; x < 8; x++) {
            sb.append((char) ((int) (Math.random() * 26) + 97));
        }
        return (sb.toString());
    }
}
