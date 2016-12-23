package com.asia_auto.data.dao;

import com.asia_auto.data.entity.Element;
import com.asia_auto.data.entity.MainElement;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Игорь on 28.09.2016.
 */
public class ElementEntityDao {
    Logger logger = Logger.getLogger(ElementEntityDao.class.getName());

    public <T> T getElementById(Class<T> clazz, long id) {
        logger.log(Level.INFO, Thread.currentThread() + "getElementById");
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        logger.log(Level.INFO, "createEntityManager");
        try {
            return em.find(clazz, id);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        } finally {
            em.close();
            logger.log(Level.INFO, Thread.currentThread() + "closeEntityManager");
        }
    }

    public <T> List<T> getAllElements(Class<T> clazz) {
        logger.log(Level.INFO, Thread.currentThread() + "getAllElements");
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        try {
            TypedQuery<T> namedQuery = em.createNamedQuery(clazz.getSimpleName() + ".getAll", clazz);
            return namedQuery.getResultList();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        } finally {
            em.close();
            logger.log(Level.INFO, Thread.currentThread() + "closeGetAllElements");
        }
    }

    public List<MainElement> getMainForDate(Date date) {
        logger.log(Level.INFO, Thread.currentThread() + "getMainForDate");
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
            logger.log(Level.INFO, Thread.currentThread() + "closeGetMainForDate");
        }
    }

    public <T> T writeElement(T element) {
        logger.log(Level.INFO, Thread.currentThread() + "writeElement");
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
            logger.log(Level.INFO, Thread.currentThread() + "closeWriteElement");
        }
    }

    public boolean deleteElement(Element element) {
        logger.log(Level.INFO, Thread.currentThread() + "deleteElement");
        EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
        Element el = em.find(element.getClass(), element.getId());
        boolean rezult = true;
        try {
            em.getTransaction().begin();
            em.remove(el);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            rezult = false;
        }
        em.close();
        logger.log(Level.INFO, Thread.currentThread() + "closeDeleteElement");
        return rezult;
    }
}
