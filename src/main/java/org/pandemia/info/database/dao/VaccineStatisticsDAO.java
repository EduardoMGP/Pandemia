package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.VaccineStatistics;

import java.util.ArrayList;
import java.util.List;

public class VaccineStatisticsDAO extends DAOMySql {
    public static List<VaccineStatistics> getVaccinesStatistics() {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return new ArrayList<>();
        return entityManager.createNamedQuery("getVaccinesStatistics", VaccineStatistics.class)
                .getResultList();
    }

}
