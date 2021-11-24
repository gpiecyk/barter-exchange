package com.gp.barter.exchange.rest.controller;

import com.gp.barter.exchange.events.OnAcceptTransactionEvent;
import com.gp.barter.exchange.events.OnMakeAnOfferEvent;
import com.gp.barter.exchange.events.OnRejectTransactionEvent;
import com.gp.barter.exchange.persistence.model.TransactionData;
import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.service.OfferService;
import com.gp.barter.exchange.persistence.service.TransactionService;
import com.gp.barter.exchange.persistence.service.UserService;
import com.gp.barter.exchange.rest.dto.Response;
import com.gp.barter.exchange.rest.dto.TransactionDto;
import com.gp.barter.exchange.rest.exception.UserDoesNotExists;
import com.gp.barter.exchange.util.constants.TransactionStatus;
import com.gp.barter.exchange.util.constants.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    private final OfferService offerService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public TransactionController(OfferService offerService, UserService userService, TransactionService transactionService, ApplicationEventPublisher eventPublisher) {
        this.offerService = offerService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/makeAnOffer", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity makeAnOffer(@RequestBody TransactionData transaction) {
        Optional<UserData> offeringUser = userService.getUserByEmail(transaction.getOfferingUser().getEmail());
        if (!offeringUser.isPresent()) {
            throw new UserDoesNotExists();
        }
        if (UserStatus.INACTIVE.equals(offeringUser.get().getStatus())) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<UserData> publisher = userService.getUserByEmail(transaction.getPublisher().getEmail());
        if (!publisher.isPresent()) {
            throw new UserDoesNotExists();
        }
        if (transactionService.userAlreadyMadeAnOffer(offeringUser.get().getId(), transaction.getOfferIdFk())) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setOfferingUser(offeringUser.get());
        transaction.setPublisher(publisher.get());
        transaction.setOffer(offerService.getById(transaction.getOfferIdFk()));
        transaction.getPictures().forEach(picture -> picture.setTransaction(transaction));
        transactionService.create(transaction);
        eventPublisher.publishEvent(new OnMakeAnOfferEvent(transaction));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/find/pending", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public List<TransactionDto> findPendingTransactions(@RequestParam("offeringId") Long offeringUserId) {
        try {
            userService.getById(offeringUserId);
        } catch (NoResultException e) {
            throw new UserDoesNotExists();
        }
        return transactionService.findOfferedTransactionsByStatus(offeringUserId, TransactionStatus.PENDING);
    }

    @RequestMapping(value = "/find/request", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public List<TransactionDto> findRequestTransactions(@RequestParam("publisherId") Long publisherId) {
        try {
            userService.getById(publisherId);
        } catch (NoResultException e) {
            throw new UserDoesNotExists();
        }
        return transactionService.findRequestTransactionsByStatus(publisherId, TransactionStatus.PENDING);
    }

    @RequestMapping(value = "/userAlreadyMadeAnOffer", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public Response userAlreadyMadeAnOffer(@RequestParam("offeringUserId") Long offeringUserId, @RequestParam("offerId") Long offerId) {
        if (transactionService.userAlreadyMadeAnOffer(offeringUserId, offerId)) {
            return new Response(HttpStatus.OK);
        }
        return new Response(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/acceptTransaction", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public void acceptTransaction(@RequestParam("transactionId") Long transactionId) {
        TransactionData transaction = transactionService.getById(transactionId);
        transaction.setStatus(TransactionStatus.ACCEPTED);
        transactionService.update(transaction);
        transactionService.delete(transaction);
        offerService.delete(transaction.getOffer());
        eventPublisher.publishEvent(new OnAcceptTransactionEvent(transaction));
    }

    @RequestMapping(value = "/rejectTransaction", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public void rejectTransaction(@RequestParam("transactionId") Long transactionId) {
        TransactionData transaction = transactionService.getById(transactionId);
        transaction.setStatus(TransactionStatus.REJECTED);
        transactionService.update(transaction);
        transactionService.delete(transaction);
        eventPublisher.publishEvent(new OnRejectTransactionEvent(transaction));
    }
}
