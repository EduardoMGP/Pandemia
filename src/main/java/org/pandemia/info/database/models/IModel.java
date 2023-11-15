package org.pandemia.info.database.models;

import org.pandemia.info.database.dao.IDao;

import java.util.List;

public abstract class IModel {

    protected final IDao dao;
    protected final IModel instance = this;

    public IModel(IDao dao) {
        this.dao = dao;
    }

    public void save() {
        this.dao.save(this);
    }

    public void update() {
        this.dao.update(this);
    }

    public boolean delete() {
        return this.dao.delete(this);
    }

    public <T> T findById(Class<T> model, Object id) {
        return this.dao.findById(model, id);
    }

    public <T> List<T> findAll(Class<T> model) {
        return this.dao.findAll(model);
    }

}
