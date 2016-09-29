import com.asia_auto.dao.ElementEntityDao;
import com.asia_auto.entity.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Игорь on 29.09.2016.
 */
public class TestDao {
    ElementEntityDao entityDao = ElementEntityDao.getInstance();
    private CopyOnWriteArraySet<Element> set=new CopyOnWriteArraySet<>();

    @Test(threadPoolSize = 100, invocationCount = 100)
    public void daoWrite() throws IOException {
        TimeElement timeElement = new TimeElement(new Time(getRandomInt(), getRandomInt(), getRandomInt()));
        MasterElement masterElement = new MasterElement(getRandomString(), getRandomString(), getRandomString(), "1", Files.readAllBytes(Paths.get("c:\\temp\\6.jpg")));
        ClientElement clientElement = new ClientElement(getRandomString(), getRandomString(), getRandomString(), null, null, null, null, null, null, null);
        MainElement mainElement = new MainElement(timeElement, clientElement, masterElement, new Date(0));
        MainElement mainElemTest = entityDao.writeElement(mainElement);
        Assert.assertTrue(mainElement.equals(mainElemTest));
        set.add(mainElemTest);
    }
    @AfterTest
    public void clear(){
        for (Element element : set) {
            entityDao.deleteElement(MainElement.class,element.getId());

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
