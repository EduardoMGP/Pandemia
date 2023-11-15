package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.User;

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
}
