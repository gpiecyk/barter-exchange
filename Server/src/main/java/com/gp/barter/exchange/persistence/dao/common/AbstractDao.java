package com.gp.barter.exchange.persistence.dao.common;


import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public abstract class AbstractDao<T extends AbstractAuditFields> implements Operations<T> {

    protected Class<T> persistentClass;

    @PersistenceContext
    protected EntityManager em;

    @Override
    public T getById(Long id) {
        return em.createQuery("from " + persistentClass.getSimpleName()
                + " t where t.id = :id " + " and t.auditRd is null", persistentClass)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("from " + persistentClass.getSimpleName() + " t where t.auditRd is null", persistentClass).getResultList();
    }

    @Override
    public void create(final T entity) {
        Objects.requireNonNull(entity);
        em.persist(entity);
    }

    @Override
    public T update(final T entity) {
        Objects.requireNonNull(entity);
        return em.merge(entity);
    }

    @Override
    public T delete(T entity) {
        Objects.requireNonNull(entity);
        entity.setAuditRd(new Date());
        return em.merge(entity);
    }

    protected void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = Objects.requireNonNull(persistentClass);
    }
}
