package com.gp.barter.exchange.persistence.service;


import com.gp.barter.exchange.persistence.dao.common.Operations;
import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.model.VerificationToken;

import java.util.Optional;

public interface UserService extends Operations<UserData> {

    void updateStatus(UserData user, String status);

    Optional<UserData> getUserByEmail(String email);

    boolean exists(UserData user);

    void changePassword(UserData user, String password);

    void createVerificationTokenForUser(UserData user, String token, String type);

    Optional<VerificationToken> getVerificationToken(String token);

    VerificationToken deleteToken(VerificationToken token);
}
