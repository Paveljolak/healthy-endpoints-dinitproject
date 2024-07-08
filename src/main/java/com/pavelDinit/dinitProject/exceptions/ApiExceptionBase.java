package com.pavelDinit.dinitProject.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.time.ZonedDateTime;
@Getter
public abstract class ApiExceptionBase extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ApiExceptionBase(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}