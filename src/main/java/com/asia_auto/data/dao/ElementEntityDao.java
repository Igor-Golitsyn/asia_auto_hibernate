package com.asia_auto.data.dao;

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
    private EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();

    private ElementEntityDao() {
    }

    private static class SingletonHelper {
        private static final ElementEntityDao INSTANCE = new ElementEntityDao();
    }

    public static ElementEntityDao getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public synchronized <T> T getElementById(Class<T> clazz, long id) {
        try {
            return em.find(clazz, id);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        }
    }

    public synchronized <T> List<T> getAllElements(Class<T> clazz) {
        try {
            TypedQuery<T> namedQuery = em.createNamedQuery(clazz.getSimpleName() + ".getAll", clazz);
            return namedQuery.getResultList();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        }
    }

    public synchronized List<MainElement> getMainForDate(Date date) {
        try {
            Query query = em.createQuery("Select e from MainElement e where e.date = :date");
            query.setParameter("date", date);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        }
    }

    public synchronized <T> T writeElement(T element) {
        try {
            em.getTransaction().begin();
            element = em.merge(element);
            em.getTransaction().commit();
            return element;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            return null;
        }
    }

    public synchronized <T> boolean deleteElement(Class<T> clazz, long id) {
        boolean rezult = true;
        try {
            em.getTransaction().begin();
            em.remove(getElementById(clazz, id));
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            rezult = false;
        }
        return rezult;
    }
}
