package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.CovidCase;

import java.util.ArrayList;
import java.util.List;

public class CovidCaseDAO extends DAOMySql {

    public static List<CovidCase> getCasesBetweenDate(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return new ArrayList<>();
        return entityManager.createQuery(
                        """
                                SELECT c FROM CovidCase c
                                INNER JOIN Neighborhood n ON c.neighborhood.id = n.id
                                JOIN FETCH c.neighborhood
                                WHERE c.case_date BETWEEN :date AND :date1
                                AND c.status IN ('confirmed', 'deceased')
                                ORDER BY c.case_date ASC
                                """, CovidCase.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getResultList();
    }

    public static List<CovidCase> getAllCases(int page, int pageSize, String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return new ArrayList<>();
        return entityManager.createQuery(
                        """
                                SELECT c FROM CovidCase c
                                INNER JOIN Neighborhood n ON c.neighborhood.id = n.id
                                INNER JOIN User u ON c.user.id = u.id
                                JOIN FETCH c.neighborhood
                                JOIN FETCH c.user
                                WHERE u.name LIKE :searchTerm
                                                OR u.email LIKE :searchTerm
                                                OR n.name LIKE :searchTerm
                                ORDER BY c.case_date ASC
                                """, CovidCase.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .setMaxResults(pageSize)
                .setFirstResult(page * pageSize)
                .getResultList();
    }

    public static int getCasesCount(String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return 0;
        return entityManager.createQuery(
                        """
                                SELECT COUNT(c) FROM CovidCase c
                                INNER JOIN Neighborhood n ON c.neighborhood.id = n.id
                                INNER JOIN User u ON c.user.id = u.id
                                WHERE u.name LIKE :searchTerm
                                    OR u.email LIKE :searchTerm
                                    OR n.name LIKE :searchTerm
                               
                                """, Long.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getSingleResult().intValue();
    }
}
