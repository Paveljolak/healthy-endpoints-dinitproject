package com.pavelDinit.dinitProject.exceptions.conflict;

import com.pavelDinit.dinitProject.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends ApiExceptionBase {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT, ZonedDateTime.now());
    }
}