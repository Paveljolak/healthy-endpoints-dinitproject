package com.pavel.dinit.project.exceptions.conflict;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public class Conflict extends ApiExceptionBase {

    public Conflict(String message) {
        super(message, HttpStatus.CONFLICT, ZonedDateTime.now());
    }
}