package com.gp.barter.exchange.events;


import com.gp.barter.exchange.persistence.model.UserData;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final UserData user;
    private final String appUrl;

    public OnRegistrationCompleteEvent(UserData user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public UserData getUser() {
        return user;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
