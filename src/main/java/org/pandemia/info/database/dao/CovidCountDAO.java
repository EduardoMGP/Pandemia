package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.CovidCount;

import java.util.List;

public class CovidCountDAO extends DAOMySql {

    public static List<CovidCount> getCountBetweenDates(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createNamedQuery("getCasesByDate", CovidCount.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getResultList();
    }

    public static CovidCount getCasesCount(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createNamedQuery("getCasesCount", CovidCount.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getSingleResult();
    }

}
