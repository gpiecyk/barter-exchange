package com.gp.barter.exchange.persistence.dao.common;


import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;

import java.util.List;

public interface Operations<T extends AbstractAuditFields> {

    T getById(final Long id);

    List<T> findAll();

    void create(final T entity);

    T update(final T entity);

    T delete(T entity);
}
