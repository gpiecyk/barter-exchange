package com.gp.barter.exchange.events.listener;

import com.gp.barter.exchange.events.OnMakeAnOfferEvent;
import com.gp.barter.exchange.util.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MakeAnOfferListener implements ApplicationListener<OnMakeAnOfferEvent> {

    private static final String NOTIFICATIONS = "notifications/";
    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public MakeAnOfferListener(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(final OnMakeAnOfferEvent event) {
        pushNotification(event);
        sendEmailNotification(event);
    }

    private void pushNotification(final OnMakeAnOfferEvent event) {
        String path = NOTIFICATIONS + event.getTransaction().getPublisher().getId();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("show", "true");
        FirebaseUtil.saveData(path, data);
    }

    private void sendEmailNotification(final OnMakeAnOfferEvent event) {
        final SimpleMailMessage email = constructEmailMessage(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnMakeAnOfferEvent event) {
        final String recipientAddress = event.getTransaction().getPublisher().getEmail();
        final String subject = "Request for exchange";
        final String message = "Hi " + event.getTransaction().getPublisher().getFirstName() +
                "!\n\nYou have one new request to exchange your product: " +
                event.getTransaction().getOffer().getTitle() + "\n" +
                "Please log in to Barter Exchange to Accept or Reject the offer.\n\n" +
                "Regards,\n" +
                "Barter Exchange";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
