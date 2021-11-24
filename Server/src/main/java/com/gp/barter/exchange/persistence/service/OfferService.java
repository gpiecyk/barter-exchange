package com.gp.barter.exchange.persistence.service;


import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.OfferData;

import java.util.List;

public interface OfferService extends Operations<OfferData> {

    List<OfferData> findPaginated(int page, int size);

    List<OfferData> findSearchPaginated(int page, int size, String titlePattern);

    List<String> findSearchOfferTitles(String titlePattern);

    Long getOffersCount();

    Long getSearchOffersCount(String titlePattern);

    List<OfferData> findByUserId(Long id);

    List<OfferData> findOldOffers();
}
