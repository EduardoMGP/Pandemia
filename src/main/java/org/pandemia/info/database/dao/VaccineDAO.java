package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.UsersVaccine;
import org.pandemia.info.database.models.Vaccine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VaccineDAO extends DAOMySql {

    public static int getVaccinesCount(String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return 0;
        return entityManager.createQuery(
                        """
                                SELECT COUNT(v) FROM Vaccine v
                                WHERE v.name LIKE :searchTerm
                                    OR v.manufacturer LIKE :searchTerm
                                    OR v.batch LIKE :searchTerm
                               
                                """, Long.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getSingleResult().intValue();
    }

    public static List<Vaccine> getAllVaccines(int page, int limit, String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createQuery(
                        """
                                SELECT v FROM Vaccine v
                                WHERE v.name LIKE :searchTerm
                                    OR v.manufacturer LIKE :searchTerm
                                    OR v.batch LIKE :searchTerm
                                ORDER BY v.name ASC
                                """, Vaccine.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .setMaxResults(limit)
                .setFirstResult(page * limit)
                .getResultList();
    }

    public static int getUsersVaccinesCount(String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return 0;
        return entityManager.createQuery(
                        """
                                SELECT COUNT(uv) FROM UsersVaccine uv
                                INNER JOIN uv.user u ON uv.user_id = u.id
                                INNER JOIN uv.vaccine v ON uv.vaccine_id = v.id
                                WHERE u.name LIKE :searchTerm
                                    OR u.email LIKE :searchTerm
                                    OR v.name LIKE :searchTerm
                                    OR v.manufacturer LIKE :searchTerm
                                    OR v.batch LIKE :searchTerm
                                """, Long.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getSingleResult().intValue();
    }

    public static List<UsersVaccine> getAllUsersVaccines(int page, int limit, String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createQuery(
                        """
                                SELECT uv FROM UsersVaccine uv
                                INNER JOIN uv.user u ON uv.user_id = u.id
                                INNER JOIN uv.vaccine v ON uv.vaccine_id = v.id
                                JOIN FETCH uv.user
                                JOIN FETCH uv.vaccine
                                WHERE u.name LIKE :searchTerm
                                    OR u.email LIKE :searchTerm
                                    OR v.name LIKE :searchTerm
                                    OR v.manufacturer LIKE :searchTerm
                                    OR v.batch LIKE :searchTerm
                                ORDER BY uv.date DESC
                                """, UsersVaccine.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .setMaxResults(limit)
                .setFirstResult(page * limit)
                .getResultList();
    }

    public static List<Vaccine> getAll() {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return new ArrayList<>();
        return entityManager.createQuery(
                        """
                                SELECT v FROM Vaccine v
                                ORDER BY v.name ASC
                                """, Vaccine.class)
                .getResultList();
    }


}
