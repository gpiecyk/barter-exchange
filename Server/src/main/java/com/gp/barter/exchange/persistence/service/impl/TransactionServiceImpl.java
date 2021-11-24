package com.gp.barter.exchange.persistence.service.impl;

import com.google.common.base.Splitter;
import com.gp.barter.exchange.persistence.dao.TransactionDao;
import com.gp.barter.exchange.persistence.dao.TransactionPictureDao;
import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.TransactionData;
import com.gp.barter.exchange.persistence.service.TransactionService;
import com.gp.barter.exchange.persistence.service.common.AbstractService;
import com.gp.barter.exchange.rest.dto.PictureDto;
import com.gp.barter.exchange.rest.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class TransactionServiceImpl extends AbstractService<TransactionData> implements TransactionService {

    private final TransactionDao transactionDao;
    private final TransactionPictureDao transactionPictureDao;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao, TransactionPictureDao transactionPictureDao) {
        this.transactionDao = transactionDao;
        this.transactionPictureDao = transactionPictureDao;
    }

    @Override
    protected Operations<TransactionData> getDao() {
        return transactionDao;
    }

    @Override
    public void create(TransactionData entity) {
        super.create(entity);
        entity.getPictures().forEach(transactionPictureDao::create);
    }

    @Override
    public TransactionData delete(TransactionData entity) {
        entity.getPictures().forEach(transactionPictureDao::delete);
        return super.delete(entity);
    }

    @Override
    public List<TransactionDto> findOfferedTransactionsByStatus(Long offeringUserId, String status) {
        List<TransactionData> transactions = transactionDao.findOfferedTransactionsByStatus(offeringUserId, status);
        List<TransactionDto> result = new LinkedList<>();
        transactions.forEach(transaction -> result.add(repackTransactionToDto(transaction)));
        return result;
    }

    private TransactionDto repackTransactionToDto(TransactionData transactionData) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transactionData.getId());
        transactionDto.setMessage(transactionData.getMessage());
        transactionDto.setOfferId(transactionData.getOfferIdFk());
        transactionDto.setOfferingUserEmail(transactionData.getOfferingUser().getEmail());
        transactionDto.setPublisherUserEmail(transactionData.getPublisher().getEmail());
        transactionDto.setOfferTitle(transactionData.getOffer().getTitle());
        Set<PictureDto> urls = new HashSet<>();
        transactionData.getPictures().forEach(pictureData -> {
            PictureDto picture = new PictureDto();
            picture.setUrl(pictureData.getPicture());
            urls.add(picture);
        });
        transactionDto.setUrls(urls);
        Iterable<String> splitter = Splitter.on(";").omitEmptyStrings().trimResults().split(transactionData.getItemsForExchange());
        splitter.forEach(s -> transactionDto.getItemsForExchange().add(s));
        return transactionDto;
    }

    @Override
    public List<TransactionDto> findRequestTransactionsByStatus(Long publisherId, String status) {
        List<TransactionData> transactions = transactionDao.findRequestTransactionsByStatus(publisherId, status);
        List<TransactionDto> result = new LinkedList<>();
        transactions.forEach(transaction -> result.add(repackTransactionToDto(transaction)));
        return result;
    }

    @Override
    public boolean userAlreadyMadeAnOffer(Long offeringUserId, Long offerId) {
        return transactionDao.findTransaction(offeringUserId, offerId).isPresent();
    }
}
