package com.gp.barter.exchange.persistence.service;


import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.TransactionData;
import com.gp.barter.exchange.rest.dto.TransactionDto;

import java.util.List;

public interface TransactionService extends Operations<TransactionData> {

    List<TransactionDto> findOfferedTransactionsByStatus(Long offeringId, String status);

    List<TransactionDto> findRequestTransactionsByStatus(Long publisherId, String status);

    boolean userAlreadyMadeAnOffer(Long offeringUserId, Long offerId);
}
