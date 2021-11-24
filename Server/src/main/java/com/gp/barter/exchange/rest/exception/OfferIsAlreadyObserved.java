package com.gp.barter.exchange.rest.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Offer is already observed")
public class OfferIsAlreadyObserved extends RuntimeException {

}
