package com.gp.barter.exchange.events.listener;


import com.gp.barter.exchange.events.OnResetPasswordRequestEvent;
import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.service.UserService;
import com.gp.barter.exchange.util.constants.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordRequestEvent> {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public ResetPasswordListener(Environment env, JavaMailSender mailSender, UserService userService) {
        this.env = env;
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(OnResetPasswordRequestEvent event) {
        resetPassword(event);
    }

    private void resetPassword(final OnResetPasswordRequestEvent event) {
        final UserData user = event.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token, TokenType.RESET_PASSWORD);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnResetPasswordRequestEvent event, final UserData user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Reset Password";
        final String confirmationUrl = event.getAppUrl() + "/change/password?token=" + token;
        final String message = "Click on the link below to change your password:\n";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        System.setProperty("line.separator", "\r\n");
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
