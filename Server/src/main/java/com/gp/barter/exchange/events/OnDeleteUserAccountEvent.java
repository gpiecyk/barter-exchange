package com.gp.barter.exchange.events;

import com.gp.barter.exchange.persistence.model.UserData;
import org.springframework.context.ApplicationEvent;

public class OnDeleteUserAccountEvent extends ApplicationEvent {

    private final UserData user;

    public OnDeleteUserAccountEvent(UserData user) {
        super(user);
        this.user = user;
    }

    public UserData getUser() {
        return user;
    }
}
