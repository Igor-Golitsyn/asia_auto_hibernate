package com.asia_auto.dao;

import com.asia_auto.entity.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Игорь on 30.09.2016.
 */
public class ElementEntityDaoTest {
    ElementEntityDao entityDao = ElementEntityDao.getInstance();
    private CopyOnWriteArraySet<Element> set = new CopyOnWriteArraySet<>();
    private TimeElement timeElement;

    @BeforeTest
    public void insertTime() {
        timeElement = entityDao.writeElement(new TimeElement(new Time(getRandomInt(), getRandomInt(), getRandomInt())));
    }

    @Test(threadPoolSize = 100, invocationCount = 100, priority = 1)
    public void daoWrite() throws IOException {
        MasterElement masterElement = new MasterElement(getRandomString(), getRandomString(), getRandomString(), "1", null);
        ClientElement clientElement = new ClientElement(getRandomString(), getRandomString(), getRandomString(), null, null, null, null, null, null, null);
        MainElement mainElement = new MainElement(timeElement, clientElement, masterElement, new Date(0));
        MainElement mainElemTest = entityDao.writeElement(mainElement);
        timeElement = mainElemTest.getTime();
        Assert.assertTrue(mainElement.equals(mainElemTest));
        set.add(mainElemTest);
    }

    @Test(priority = 2)
    public void getAll() {
        Assert.assertTrue(set.size() == entityDao.getAllElements(MainElement.class).size());
    }

    @AfterTest
    public void clear() {
        for (Element element : set) {
            entityDao.deleteElement(element.getClass(), element.getId());
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