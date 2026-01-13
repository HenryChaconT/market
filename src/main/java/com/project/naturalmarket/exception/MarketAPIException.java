package com.project.naturalmarket.exception;

import org.springframework.http.HttpStatus;

public class MarketAPIException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public MarketAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public MarketAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
