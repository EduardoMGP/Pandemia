package org.pandemia.info.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import org.pandemia.info.PandemicApplication;

public class ConnectionJPA {

    static EntityManagerFactory emf;

    private ConnectionJPA() {
        try {
            emf = Persistence.createEntityManagerFactory("pandemia");
            PandemicApplication.logger.info("Connection initialized");
        } catch (PersistenceException e) {
            PandemicApplication.logger.severe(e.getMessage());
        }
    }

    public static EntityManager entityManager() {
        try {
            return emf.createEntityManager();
        } catch (IllegalStateException e) {
            PandemicApplication.logger.severe(e.getMessage());
        }
        return null;
    }

    public static void init() {
        if (emf == null) {
            new ConnectionJPA();
        } else {
            PandemicApplication.logger.info("Connection already initialized");
        }
    }
}
