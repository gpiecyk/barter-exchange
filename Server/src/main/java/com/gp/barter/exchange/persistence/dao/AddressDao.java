package com.gp.barter.exchange.persistence.dao;


import com.gp.barter.exchange.persistence.dao.common.AbstractDao;
import com.gp.barter.exchange.persistence.model.AddressData;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDao extends AbstractDao<AddressData> {

    public AddressDao() {
        setPersistentClass(AddressData.class);
    }
}
