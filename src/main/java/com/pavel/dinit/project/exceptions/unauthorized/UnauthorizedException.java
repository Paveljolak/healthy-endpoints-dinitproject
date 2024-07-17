package com.pavel.dinit.project.exceptions.unauthorized;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ApiExceptionBase {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, ZonedDateTime.now());
    }
}
