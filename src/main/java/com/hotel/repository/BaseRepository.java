package com.hotel.repository;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public abstract class BaseRepository<T, ID extends Serializable> {
    protected final Session session;
    private final Class<T> entityClass;

    protected BaseRepository(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        session.persist(entity);
    }

    public void update(T entity) {
        session.merge(entity);
    }

    public void delete(T entity) {
        session.remove(entity);
    }

    public T findById(ID id) {
        return session.find(entityClass, id);
    }

    public List<T> findAll() {
        return session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();
    }

    public long count() {
        return session.createQuery("select count(e) from " + entityClass.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }
}
