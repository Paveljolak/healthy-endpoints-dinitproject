package com.pavel.dinit.project.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


import java.time.ZonedDateTime;
@Getter
public abstract class ApiExceptionBase extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    protected ApiExceptionBase(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }


}