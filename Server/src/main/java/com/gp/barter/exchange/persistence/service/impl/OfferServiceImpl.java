package com.gp.barter.exchange.persistence.service.impl;


import com.gp.barter.exchange.persistence.dao.OfferDao;
import com.gp.barter.exchange.persistence.dao.PictureDao;
import com.gp.barter.exchange.persistence.dao.TransactionDao;
import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.OfferData;
import com.gp.barter.exchange.persistence.model.TransactionData;
import com.gp.barter.exchange.persistence.model.WatchListData;
import com.gp.barter.exchange.persistence.service.OfferService;
import com.gp.barter.exchange.persistence.service.WatchListService;
import com.gp.barter.exchange.persistence.service.common.AbstractService;
import com.gp.barter.exchange.util.constants.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl extends AbstractService<OfferData> implements OfferService {

    private final OfferDao offerDao;
    private final PictureDao pictureDao;
    private final TransactionDao transactionDao;
    private final WatchListService watchListService;

    @Autowired
    public OfferServiceImpl(OfferDao offerDao, PictureDao pictureDao, TransactionDao transactionDao, WatchListService watchListService) {
        this.offerDao = offerDao;
        this.pictureDao = pictureDao;
        this.transactionDao = transactionDao;
        this.watchListService = watchListService;
    }

    @Override
    protected Operations<OfferData> getDao() {
        return offerDao;
    }

    @Override
    public void create(OfferData entity) {
        super.create(entity);
        entity.getPictures().forEach(pictureDao::create);
    }

    @Override
    public OfferData delete(OfferData entity) {
        deleteTransactions(entity);
        deleteObservedOffers(entity);
        entity.getPictures().forEach(pictureDao::delete);
        return super.delete(entity);
    }

    private void deleteTransactions(OfferData entity) {
        List<TransactionData> transactions = transactionDao.findTransactions(entity.getId());
        transactions.forEach(this::deleteTransaction);
    }

    private void deleteTransaction(TransactionData transaction) {
        setRejectedStatusIfPending(transaction);
        transactionDao.delete(transaction);
    }

    private void setRejectedStatusIfPending(TransactionData transaction) {
        if (TransactionStatus.PENDING.equals(transaction.getStatus())) {
            transaction.setStatus(TransactionStatus.REJECTED);
        }
    }

    private void deleteObservedOffers(OfferData entity) {
        List<WatchListData> observedOffers = watchListService.findObservedOffers(entity.getId());
        observedOffers.forEach(watchListService::delete);
    }

    @Override
    public List<OfferData> findByUserId(Long id) {
        return offerDao.findByUserId(id);
    }

    @Override
    public List<OfferData> findPaginated(int page, int size) {
        int start = page * size - size;
        return offerDao.findPaginated(start, size);
    }

    @Override
    public List<OfferData> findSearchPaginated(int page, int size, String titlePattern) {
        int start = page * size - size;
        return offerDao.findSearchPaginated(start, size, titlePattern);
    }

    @Override
    public List<String> findSearchOfferTitles(String titlePattern) {
        return offerDao.findSearchOfferTitles(titlePattern);
    }

    @Override
    public Long getOffersCount() {
        return offerDao.getOffersCount();
    }

    @Override
    public Long getSearchOffersCount(String titlePattern) {
        return offerDao.getSearchOffersCount(titlePattern);
    }

    @Override
    public List<OfferData> findOldOffers() {
        return offerDao.findOldOffers();
    }
}
