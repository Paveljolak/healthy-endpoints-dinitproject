package com.pavelDinit.dinitProject.exceptions.old;

import com.pavelDinit.dinitProject.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApiExceptionBase {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ZonedDateTime.now());
    }
}
