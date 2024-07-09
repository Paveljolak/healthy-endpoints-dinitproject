package com.pavelDinit.dinitProject.exceptions.badrequest;

import com.pavelDinit.dinitProject.exceptions.ApiExceptionBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiBadRequest extends ApiExceptionBase {

    public ApiBadRequest(String message) {
        super(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now());
    }
}
