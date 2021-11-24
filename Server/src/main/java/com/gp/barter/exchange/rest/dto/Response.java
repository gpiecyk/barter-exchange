package com.gp.barter.exchange.rest.dto;

import org.springframework.http.HttpStatus;

public class Response {

    private HttpStatus httpStatus;
    private int statusValue;

    public Response(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.statusValue = httpStatus.value();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(int statusValue) {
        this.statusValue = statusValue;
    }
}
