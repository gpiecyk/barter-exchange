package com.gp.barter.exchange.persistence.service.impl;

import com.gp.barter.exchange.persistence.dao.TransactionDao;
import com.gp.barter.exchange.persistence.dao.UserDao;
import com.gp.barter.exchange.persistence.dao.VerificationTokenDao;
import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.*;
import com.gp.barter.exchange.persistence.service.*;
import com.gp.barter.exchange.persistence.service.common.AbstractService;
import com.gp.barter.exchange.util.constants.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractService<UserData> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationTokenDao verificationTokenDao;

    @Autowired
    private AddressService addressService;

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected Operations<UserData> getDao() {
        return userDao;
    }

    @Override
    public void create(UserData entity) {
        addressService.create(entity.getAddressData());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        super.create(entity);
    }

    @Override
    public UserData delete(UserData entity) {
        deleteTransactions(entity);
        deleteOffersObservedByUser(entity);
        deleteOffersCreatedByUser(entity);
        deleteUserAddress(entity);
        return super.delete(entity);
    }

    private void deleteTransactions(UserData entity) {
        List<TransactionData> transactions = transactionDao.findOfferedTransactionsByStatus(entity.getId(), TransactionStatus.PENDING);
        transactions.forEach(transactionDao::delete);
    }

    private void deleteOffersObservedByUser(UserData entity) {
        List<WatchListData> offersObservedByUser = watchListService.findOffersObservedByUser(entity.getId());
        offersObservedByUser.forEach(offer -> watchListService.delete(offer));
    }

    private void deleteOffersCreatedByUser(UserData entity) {
        List<OfferData> offers = offerService.findByUserId(entity.getId());
        offers.forEach(offer -> offerService.delete(offer));
    }

    private void deleteUserAddress(UserData entity) {
        addressService.delete(entity.getAddressData());
    }

    @Override
    public void updateStatus(UserData user, String status) {
        user.setStatus(status);
        userDao.update(user);
    }

    @Override
    public UserData update(UserData entity) {
        addressService.update(entity.getAddressData());
        return super.update(entity);
    }

    @Override
    public Optional<UserData> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public boolean exists(UserData user) {
        return userDao.getUserByEmail(user.getEmail()).isPresent();
    }

    @Override
    public void changePassword(UserData user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userDao.update(user);
    }

    @Override
    public void createVerificationTokenForUser(UserData user, String token, String type) {
        VerificationToken verificationToken = new VerificationToken(token, user, type);
        verificationTokenDao.create(verificationToken);
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        return verificationTokenDao.getVerificationToken(token);
    }

    @Override
    public VerificationToken deleteToken(VerificationToken token) {
        return verificationTokenDao.delete(token);
    }
}
