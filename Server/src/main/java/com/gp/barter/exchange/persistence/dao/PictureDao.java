package com.gp.barter.exchange.persistence.dao;


import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.PictureData;
import org.springframework.stereotype.Repository;

@Repository
public class PictureDao extends AbstractDao<PictureData> {

    public PictureDao() {
        setPersistentClass(PictureData.class);
    }
}
