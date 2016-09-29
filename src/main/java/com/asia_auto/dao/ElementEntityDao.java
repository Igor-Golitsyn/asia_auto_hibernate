package com.asia_auto.dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Игорь on 28.09.2016.
 */
public class ElementEntityDao {
    private EntityManager em = Persistence.createEntityManagerFactory("COLIBRI").createEntityManager();
    private static ElementEntityDao ourInstance = new ElementEntityDao();

    private ElementEntityDao() {
    }

    public static ElementEntityDao getInstance() {
        return ourInstance;
    }

    public synchronized <T> T getElementById(Class<T> clazz, long id) {
        T element = em.find(clazz, id);
        return element;

    }

    public synchronized <T> List<T> getAllElements(Class<T> clazz) {
        TypedQuery<T> namedQuery = em.createNamedQuery(clazz.getSimpleName() + ".getAll", clazz);
        return namedQuery.getResultList();

    }

    public synchronized <T> T writeElement(T element) {
        em.getTransaction().begin();
        element = em.merge(element);
        em.getTransaction().commit();
        return element;

    }

    public synchronized <T> void deleteElement(Class<T> clazz, long id) {
        try {
            em.remove(getElementById(clazz, id));
        } catch (Exception e) {
            System.out.println("=======================================================================");
            e.printStackTrace();
            System.out.println("=======================================================================");
        }
    }
}
