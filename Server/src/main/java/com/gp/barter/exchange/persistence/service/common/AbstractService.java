package com.gp.barter.exchange.persistence.service.common;

import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class AbstractService<T extends AbstractAuditFields> implements Operations<T> {

    @Override
    public T getById(Long id) {
        return getDao().getById(id);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public void create(final T entity) {
        getDao().create(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    public T delete(T entity) {
        return getDao().delete(entity);
    }

    protected abstract Operations<T> getDao();
}
