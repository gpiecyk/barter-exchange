package com.gp.barter.exchange.events.listener;


import com.gp.barter.exchange.events.OnSendQuestionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendQuestionListener implements ApplicationListener<OnSendQuestionEvent> {

    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public SendQuestionListener(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(final OnSendQuestionEvent event) {
        sendQuestion(event);
    }

    private void sendQuestion(final OnSendQuestionEvent event) {
        final SimpleMailMessage email = constructEmailMessage(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnSendQuestionEvent event) {
        final String recipientAddress = event.getUser().getEmail();
        final String subject = "Question - " + event.getOfferTitle();
        final String offerUrl = event.getAppUrl() + "/offer/" + event.getOfferId();
        final String message = "Here is a message from " + event.getSenderEmail() + " about " + event.getOfferTitle()
                + " " + offerUrl + " \n\n"
                + event.getMessage();
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
