package com.gp.barter.exchange.events;

import com.gp.barter.exchange.persistence.model.TransactionData;
import org.springframework.context.ApplicationEvent;

public class OnRejectTransactionEvent extends ApplicationEvent {

    private final TransactionData transaction;

    public OnRejectTransactionEvent(TransactionData transaction) {
        super(transaction);
        this.transaction = transaction;
    }

    public TransactionData getTransaction() {
        return transaction;
    }
}
