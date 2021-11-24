package com.gp.barter.exchange.events.listener;

import com.gp.barter.exchange.events.OnAcceptTransactionEvent;
import com.gp.barter.exchange.persistence.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class AcceptTransactionListener implements ApplicationListener<OnAcceptTransactionEvent> {

    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public AcceptTransactionListener(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(final OnAcceptTransactionEvent event) {
        sendEmailToOfferingUser(event);
        sendEmailToPublisher(event);
    }

    private void sendEmailToOfferingUser(final OnAcceptTransactionEvent event) {
        final SimpleMailMessage email = constructEmailMessageForOfferingUser(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessageForOfferingUser(final OnAcceptTransactionEvent event) {
        final String recipientAddress = event.getTransaction().getOfferingUser().getEmail();
        final String subject = "Your request was accepted!";
        final String message = "Congratulations!\n\nYour request was accepted for product: " +
                event.getTransaction().getOffer().getTitle() + "\n" +
                "Please contact " + event.getTransaction().getPublisher().getEmail() +
                " to determine shipping details. " +
                getMessageWithPhoneNumber(event.getTransaction().getPublisher()) + "\n\n" +
                "Regards,\n" +
                "Barter Exchange";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getMessageWithPhoneNumber(UserData user) {
        if (user.getPhoneNumber() != null) {
            return "You can also call him using this number: " + user.getPhoneNumber();
        }
        return "";
    }

    private void sendEmailToPublisher(final OnAcceptTransactionEvent event) {
        final SimpleMailMessage email = constructEmailMessageForPublisher(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessageForPublisher(final OnAcceptTransactionEvent event) {
        final String recipientAddress = event.getTransaction().getPublisher().getEmail();
        final String subject = "You accepted the offer!";
        final String message = "Congratulations!\n\nYou accepted the exchange for: " +
                event.getTransaction().getOffer().getTitle() + "\n" +
                "Please contact " + event.getTransaction().getOfferingUser().getEmail() +
                " to determine shipping details. " +
                getMessageWithPhoneNumber(event.getTransaction().getOfferingUser()) + "\n\n" +
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
