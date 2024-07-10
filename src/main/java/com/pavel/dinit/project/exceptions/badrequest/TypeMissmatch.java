package com.pavel.dinit.project.exceptions.badrequest;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TypeMissmatch extends ApiExceptionBase {

    public TypeMissmatch(String message) {
        super(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
    }
}


