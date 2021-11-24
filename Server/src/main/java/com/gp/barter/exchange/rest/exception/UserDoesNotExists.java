package com.gp.barter.exchange.rest.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User doesn't exists")
public class UserDoesNotExists extends RuntimeException {

}
