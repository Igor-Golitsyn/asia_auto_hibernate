package com.asia_auto.data.dao;

import com.asia_auto.data.entity.Element;
import com.asia_auto.data.entity.MainElement;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 28.09.2016.
 */
public class ElementEntityDao {
    Logger logger = Logger.getLogger(ElementEntityDao.class.getName());

    private ElementEntityDao() {
    }

    private static class SingletonHelper {
        private static final ElementEntityDao INSTANCE = new ElementEntityDao();
    }

    public static ElementEntityDao getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public synchronized List<MainElement> getMainForDate(Date date) {
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        try {
            Query query = em.createQuery("Select e from MainElement e where e.date = :date");
            query.setParameter("date", date);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        } finally {
            em.close();
        }
    }

    public synchronized <T> T writeElement(T element) {
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        try {
            em.getTransaction().begin();
            element = em.merge(element);
            em.getTransaction().commit();
            return element;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        } finally {
            em.close();
        }
    }

    public synchronized boolean deleteElement(Element element) {
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        Element el = em.find(element.getClass(), element.getId());
        boolean rezult = true;
        try {
            em.getTransaction().begin();
            em.remove(el);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            e.printStackTrace();
            rezult = false;
        }
        em.close();
        return rezult;
    }
}
