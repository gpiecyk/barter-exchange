package com.gp.barter.exchange.persistence.service.impl;


import com.gp.barter.exchange.persistence.dao.AddressDao;
import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.AddressData;
import com.gp.barter.exchange.persistence.service.AddressService;
import com.gp.barter.exchange.persistence.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends AbstractService<AddressData> implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    protected Operations<AddressData> getDao() {
        return addressDao;
    }
}
