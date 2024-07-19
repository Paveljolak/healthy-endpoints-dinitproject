package com.pavel.dinit.project.exceptions.badrequest;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public class ApiBadRequest extends ApiExceptionBase {

    public ApiBadRequest(String message) {
        super(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
    }
}
