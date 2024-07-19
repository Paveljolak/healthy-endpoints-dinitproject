package com.pavel.dinit.project.exceptions.badrequest;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public class TypeMissmatch extends ApiExceptionBase {

    public TypeMissmatch(String message) {
        super(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
    }
}


