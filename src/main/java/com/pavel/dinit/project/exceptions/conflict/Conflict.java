package com.pavel.dinit.project.exceptions.conflict;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ResponseStatus(HttpStatus.CONFLICT)
public class Conflict extends ApiExceptionBase {

    public Conflict(String message) {
        super(message, HttpStatus.CONFLICT, ZonedDateTime.now());
    }
}