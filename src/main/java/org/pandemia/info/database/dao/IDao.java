package org.pandemia.info.database.dao;

import org.pandemia.info.database.models.IModel;

import java.util.List;

public interface IDao {

    void save(IModel model);
    void update(IModel model);
    boolean delete(IModel model);
    <T> T findById(Class<T> model, Object id);

    <T> List<T> findAll(Class<T> model);
}
