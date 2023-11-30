package org.pandemia.info.database.dao;


import jakarta.persistence.EntityManager;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.Neighborhood;

import java.util.List;

public class NeighborgoodDAO extends DAOMySql {


    public static List<Neighborhood> getAll(int page, int pageSize, String searchTerm) {
        EntityManager entityManager = ConnectionJPA.entityManager();
        if (entityManager == null) return null;
        return entityManager.createQuery(
                        """
                                SELECT n FROM Neighborhood n
                                WHERE n.name LIKE :searchTerm
                                """, Neighborhood.class)
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
                                SELECT COUNT(n) FROM Neighborhood n
                                WHERE n.name LIKE :searchTerm                             
                                """, Long.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getSingleResult().intValue();
    }

}
