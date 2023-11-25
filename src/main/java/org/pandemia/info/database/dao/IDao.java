package org.pandemia.info.database.dao;

import java.util.List;

public interface IDao {

    void save();
    void update();
    boolean delete();
    <T> T findById(Class<T> model, Object id);

    <T> List<T> findAll(Class<T> model);
}
