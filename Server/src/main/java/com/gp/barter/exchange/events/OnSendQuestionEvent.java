package com.gp.barter.exchange.events;


import com.gp.barter.exchange.persistence.model.UserData;
import org.springframework.context.ApplicationEvent;

public class OnSendQuestionEvent extends ApplicationEvent {

    private final UserData user;
    private final String senderEmail;
    private final String offerTitle;
    private final String message;
    private final String appUrl;
    private final String offerId;

    public OnSendQuestionEvent(UserData user, String senderEmail, String offerTitle, String message, String appUrl, String offerId) {
        super(user);
        this.user = user;
        this.senderEmail = senderEmail;
        this.offerTitle = offerTitle;
        this.message = message;
        this.appUrl = appUrl;
        this.offerId = offerId;
    }

    public UserData getUser() {
        return user;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public String getMessage() {
        return message;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getOfferId() {
        return offerId;
    }
}
