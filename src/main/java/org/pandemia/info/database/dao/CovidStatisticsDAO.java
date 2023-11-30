package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.CovidStatistics;

import java.util.List;

public class CovidStatisticsDAO extends DAOMySql {

    public static List<CovidStatistics> getStatisticsBetweenDates(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createNamedQuery("getCasesByDate", CovidStatistics.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getResultList();
    }

    public static CovidStatistics getCasesStatistics(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createNamedQuery("getCasesStatistics", CovidStatistics.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getSingleResult();
    }

}
