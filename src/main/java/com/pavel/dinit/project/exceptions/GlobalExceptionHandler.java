package com.pavel.dinit.project.exceptions;

import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static ResponseEntity<Object> handleApiException(ApiExceptionBase e) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("status", e.getHttpStatus());
        response.put("timestamp", e.getTimestamp());

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e) {
        return handleApiException(e);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFound e) {
        return handleApiException(e);
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<Object> handleConflictException(Conflict e) {
        return handleApiException(e);
    }

    @ExceptionHandler(ApiBadRequest.class)
    public ResponseEntity<Object> handleApiBadRequestException(ApiBadRequest e) {
        return handleApiException(e);
    }

    @ExceptionHandler(TypeMissmatch.class)
    public ResponseEntity<Object> handleTypeMismatchException(TypeMissmatch e) {
        return handleApiException(e);
    }

}
