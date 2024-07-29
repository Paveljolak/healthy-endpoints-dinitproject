package com.pavel.dinit.project.exceptions;

import com.pavel.dinit.project.exceptions.badrequest.ApiBadRequest;
import com.pavel.dinit.project.exceptions.badrequest.TypeMissmatch;
import com.pavel.dinit.project.exceptions.conflict.Conflict;
import com.pavel.dinit.project.exceptions.notfound.ResourceNotFound;
import com.pavel.dinit.project.exceptions.unauthorized.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return "Unauthorized: " + ex.getMessage();
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFound e) {
        return handleApiException(e);
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<Map<String, String>> handleConflict(Conflict ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ApiBadRequest.class)
    public ResponseEntity<Map<String, String>> handleApiBadRequest(ApiBadRequest ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(TypeMissmatch.class)
    public ResponseEntity<Object> handleTypeMismatchException(TypeMissmatch e) {
        return handleApiException(e);
    }

}
