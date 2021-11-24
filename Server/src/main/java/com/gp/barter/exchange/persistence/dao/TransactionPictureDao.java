package com.gp.barter.exchange.persistence.dao;

import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.TransactionPictureData;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionPictureDao extends AbstractDao<TransactionPictureData> {

    public TransactionPictureDao() {
        setPersistentClass(TransactionPictureData.class);
    }
}
