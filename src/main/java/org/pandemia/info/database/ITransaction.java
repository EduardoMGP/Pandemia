package org.pandemia.info.database;

import jakarta.persistence.EntityManager;

public interface ITransaction {

    void execute(EntityManager manager);

}
