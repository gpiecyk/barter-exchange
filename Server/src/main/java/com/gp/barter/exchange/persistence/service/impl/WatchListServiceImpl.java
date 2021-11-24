package com.gp.barter.exchange.persistence.service.impl;


import com.gp.barter.exchange.persistence.dao.WatchListDao;
import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.WatchListData;
import com.gp.barter.exchange.persistence.service.WatchListService;
import com.gp.barter.exchange.persistence.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchListServiceImpl extends AbstractService<WatchListData> implements WatchListService {

    @Autowired
    private WatchListDao watchListDao;

    @Override
    protected Operations<WatchListData> getDao() {
        return watchListDao;
    }

    @Override
    public Optional<WatchListData> findObservedOffer(Long offerId, Long userId) {
        return watchListDao.findObservedOffer(offerId, userId);
    }

    @Override
    public List<WatchListData> findOffersObservedByUser(Long userId) {
        return watchListDao.findOffersObservedByUser(userId);
    }

    @Override
    public List<WatchListData> findObservedOffers(Long offerId) {
        return watchListDao.findObservedOffers(offerId);
    }
}
