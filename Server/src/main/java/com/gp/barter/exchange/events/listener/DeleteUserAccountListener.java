package com.gp.barter.exchange.events.listener;

import com.gp.barter.exchange.events.OnDeleteUserAccountEvent;
import com.gp.barter.exchange.util.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserAccountListener implements ApplicationListener<OnDeleteUserAccountEvent> {

    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public DeleteUserAccountListener(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(final OnDeleteUserAccountEvent event) {
        sendEmailNotification(event);
        FirebaseUtil.deleteUserAccount(event.getUser());
    }

    private void sendEmailNotification(final OnDeleteUserAccountEvent event) {
        final SimpleMailMessage email = constructEmailMessageForOfferingUser(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessageForOfferingUser(final OnDeleteUserAccountEvent event) {
        final String recipientAddress = event.getUser().getEmail();
        final String subject = "Your account was removed";
        final String message = "Hi " + event.getUser().getFirstName() + "!\n\nYour account was removed. " +
                "Thank you for using our service. Hope to see you soon :)\n\n" +
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
