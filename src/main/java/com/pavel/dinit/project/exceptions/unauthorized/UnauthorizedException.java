package com.pavel.dinit.project.exceptions.unauthorized;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public class UnauthorizedException extends ApiExceptionBase {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, ZonedDateTime.now());
    }
}
