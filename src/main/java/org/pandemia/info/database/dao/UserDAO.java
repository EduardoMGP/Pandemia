package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.User;

import java.util.List;

public class UserDAO extends DAOMySql {


    public User findByEmail(String email) {
        EntityManager em = ConnectionJPA.entityManager();
        if (em == null)
            return null;
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public static List<User> searchByName(String name) {
        EntityManager em = ConnectionJPA.entityManager();
        if (em == null)
            return null;
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.name LIKE :name OR u.email LIKE :email", User.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("email", "%" + name + "%")
                    .setMaxResults(25)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<User> getAll(int page, int pageSize, String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createQuery(
                        """
                                SELECT u FROM User u
                                INNER JOIN FETCH u.neighborhoods
                                WHERE u.name LIKE :searchTerm
                                OR u.email LIKE :searchTerm
                                ORDER BY u.name
                                """, User.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .setMaxResults(pageSize)
                .setFirstResult(page * pageSize)
                .getResultList();
    }

    public static int getCount(String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return 0;
        return entityManager.createQuery(
                        """
                                SELECT COUNT(n) FROM User n
                                WHERE n.name LIKE :searchTerm
                                OR n.email LIKE :searchTerm                       
                                """, Long.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getSingleResult().intValue();
    }
}
