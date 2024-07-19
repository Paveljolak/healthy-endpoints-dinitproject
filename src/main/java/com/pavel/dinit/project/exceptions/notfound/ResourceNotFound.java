package com.pavel.dinit.project.exceptions.notfound;

import com.pavel.dinit.project.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;



public class ResourceNotFound extends ApiExceptionBase {

    public ResourceNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND, ZonedDateTime.now());
    }
}
