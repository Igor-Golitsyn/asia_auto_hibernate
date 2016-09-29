import data.HibernateUtil;
import data.elements.ClientElement;
import data.elements.MainElement;
import data.elements.MasterElement;
import data.elements.TimeElement;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by Игорь on 28.09.2016.
 */
public class Dddd {
    public static void main(String[] args) throws IOException {
        writeToSql();
        readFromSql();
    }

    private static void writeToSql() throws IOException {
        TimeElement timeElement = new TimeElement(new Time(0));
        MasterElement masterElement = new MasterElement("Ivan", "Ivanovich", "Ivanov", "1", Files.readAllBytes(new File("c:\\temp\\6.jpg").toPath()));
        ClientElement clientElement = new ClientElement("1111111", "11111111", "111111", null, null, null, null, null, null, null);
        MainElement element = new MainElement(timeElement, clientElement, masterElement, new Date(111));
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        em.getTransaction().begin();
        em.merge(element);
        em.getTransaction().commit();
        timeElement = getData(TimeElement.class, 1);
        masterElement = getData(MasterElement.class, 1);
        ClientElement clientElement1 = new ClientElement("222222", "2222222", "2222222", null, null, null, null, null, null, null);
        MainElement element1 = new MainElement(timeElement, clientElement1, masterElement, new Date(111));
        em.getTransaction().begin();
        em.merge(element1);
        em.getTransaction().commit();
    }

    private static void readFromSql() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        MainElement mainElement = (MainElement) session.get(MainElement.class, 1L);
        Hibernate.initialize(mainElement);
        System.out.println(mainElement);
        mainElement = (MainElement) session.get(MainElement.class, 2L);
        Hibernate.initialize(mainElement);
        System.out.println(mainElement);
    }

    private static <T> T getData(Class<T> clazz, long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (T) session.get(clazz, id);
    }
}
