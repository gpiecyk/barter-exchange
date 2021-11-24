package com.gp.barter.exchange.events.listener;

import com.gp.barter.exchange.events.OnRejectTransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RejectTransactionListener implements ApplicationListener<OnRejectTransactionEvent> {

    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public RejectTransactionListener(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(final OnRejectTransactionEvent event) {
        sendEmailToOfferingUser(event);
    }

    private void sendEmailToOfferingUser(final OnRejectTransactionEvent event) {
        final SimpleMailMessage email = constructEmailMessageForOfferingUser(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessageForOfferingUser(final OnRejectTransactionEvent event) {
        final String recipientAddress = event.getTransaction().getOfferingUser().getEmail();
        final String subject = "Your request was rejected!";
        final String message = "Hi " + event.getTransaction().getOfferingUser().getFirstName() +
                "!\n\nUnfortunately your request was rejected for product: " +
                event.getTransaction().getOffer().getTitle() + "\n\n" +
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
