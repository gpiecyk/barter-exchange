package com.gp.barter.exchange.persistence.service;


import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.WatchListData;

import java.util.List;
import java.util.Optional;

public interface WatchListService extends Operations<WatchListData> {

    Optional<WatchListData> findObservedOffer(Long offerId, Long userId);

    List<WatchListData> findOffersObservedByUser(Long userId);

    List<WatchListData> findObservedOffers(Long offerId);
}
