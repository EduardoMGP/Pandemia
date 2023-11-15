package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.CovidCase;
import org.pandemia.info.database.models.CovidCount;

import java.util.List;

public class CovidCaseDAO extends DAOMySql {

    public List<CovidCount> getCasesByDate(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createNamedQuery("getCasesByDate", CovidCount.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getResultList();
    }

    public CovidCount getCasesCount(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createNamedQuery("getCasesCount", CovidCount.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getSingleResult();
    }

    public List<CovidCase> getCases(String date, String date1) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createQuery(
                """
                        SELECT c FROM CovidCase c
                        WHERE c.covid_case_date BETWEEN :date AND :date1
                        AND c.covid_case_status IN ('confirmed', 'deceased')
                        ORDER BY c.covid_case_date ASC
                        """, CovidCase.class)
                .setParameter("date", date)
                .setParameter("date1", date1).getResultList();
    }
}
