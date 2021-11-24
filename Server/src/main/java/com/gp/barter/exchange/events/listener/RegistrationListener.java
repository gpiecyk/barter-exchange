package com.gp.barter.exchange.events.listener;


import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.service.UserService;
import com.gp.barter.exchange.events.OnRegistrationCompleteEvent;
import com.gp.barter.exchange.util.constants.TokenType;
import com.gp.barter.exchange.util.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String NOTIFICATIONS = "notifications/";
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public RegistrationListener(UserService userService, JavaMailSender mailSender, Environment env) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final UserData user = event.getUser();
        sendEmailNotification(event, user, generateToken(user));
        createIdInFirebase(user);
    }

    private String generateToken(UserData user) {
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token, TokenType.REGISTER);
        return token;
    }

    private void sendEmailNotification(OnRegistrationCompleteEvent event, UserData user, String token) {
        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final UserData user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registration/confirm?token=" + token;
        final String message = "You registered successfully. Click on the link below to activate your account:\n";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl + "\n\nRegards,\nBarter Exchange");
        System.setProperty("line.separator", "\r\n");
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private void createIdInFirebase(UserData user) {
        String path = NOTIFICATIONS + user.getId();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("show", "false");
        FirebaseUtil.saveData(path, data);
    }
}
