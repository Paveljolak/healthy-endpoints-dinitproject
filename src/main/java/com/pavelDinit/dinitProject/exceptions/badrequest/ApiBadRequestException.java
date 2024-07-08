package com.pavelDinit.dinitProject.exceptions.old;

import com.pavelDinit.dinitProject.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiBadRequestException extends ApiExceptionBase {

    public ApiBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
    }
}
