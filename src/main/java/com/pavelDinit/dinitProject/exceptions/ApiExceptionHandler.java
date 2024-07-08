package com.pavelDinit.dinitProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
@RestControllerAdvice
public class ApiExceptionHandler {



    @ExceptionHandler(value = {ApiBadRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiBadRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }



    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, notFound);
    }


    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<Object> handleConflictException(ConflictException e) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(
                e.getMessage(),
                conflict,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, conflict);
    }
}