package org.pandemia.info.database.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.ITransaction;
import org.pandemia.info.exceptions.TransactionException;

import java.util.List;

public abstract class DAOMySql implements IDao {

    public void update() {
        DAOMySql.inTransaction((manager) -> manager.merge(this));
    }

    public boolean delete() {
        return DAOMySql.inTransaction((manager) -> {
            try {
                Object entity = manager.find(getClass(), getClass().getMethod("getId").invoke(this));
                manager.remove(entity);
            } catch (Exception e) {
                throw new TransactionException("Não foi possível encontrar o objeto para deletar");
            }
        });
    }

    public <T> T findById(Class<T> model, Object id) {

        EntityManager em = ConnectionJPA.entityManager();
        if (em != null) {

            try {
                return em.find(model, id);
            } catch (Exception e) {
                return null;
            }

        }

        return null;

    }

    public <T> List<T> findAll(Class<T> model) {

        EntityManager em = ConnectionJPA.entityManager();

        if (em != null) {

            try {

                // Buscar o nome da entidade através da anotação @Entity ou @Table ou pelo nome da classe pelo método getSimpleName()
                // para montar a query de busca
                String name = model.getAnnotation(jakarta.persistence.Entity.class).name();
                if (name.isEmpty())
                    name = model.getAnnotation(jakarta.persistence.Table.class).name();
                if (name.isEmpty())
                    name = model.getSimpleName();

                Query query = em.createQuery("SELECT " + name + " FROM " + model.getName() + " " + name);
                List<T> list = query.getResultList();
                return list;

            } catch (Exception e) {
                return null;
            }

        }

        return null;

    }

    public void save() {
        DAOMySql.inTransaction((manager) -> {
            //Se o objeto existir no entityManager significa que ele não está transiente
            //e portanto deve ser atualizado no banco de dados ao invés de persistido

            try {

                Object id = getClass().getMethod("getId").invoke(this);
                if (id != null)
                    manager.merge(this);
                else {

                    Class<?> modelClass = getClass();
                    if (this.findById(modelClass, id) != null) {
                        manager.merge(this);
                    } else {
                        manager.persist(this);
                    }

                }

            } catch (Exception e) {
                throw new TransactionException(e.getMessage());
            }

        });
    }

    public static boolean inTransaction(ITransaction transaction) {
        EntityManager em = ConnectionJPA.entityManager();
        if (em != null) {
            try {
                em.getTransaction().begin();
                transaction.execute(em);
                em.getTransaction().commit();
                return true;
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw new TransactionException(e.getMessage());
            } finally {
                em.close();
            }
        }
        return false;
    }

    public void refresh() {
        DAOMySql.inTransaction((manager) -> manager.refresh(this));
    }
}
