package com.example.demo.advice;

import com.example.demo.Exception.InvalidAgeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseStatusException handleResponseStatusException(ResponseStatusException e) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(InvalidAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleInvalidAgeException(InvalidAgeException e) {
        return new ResponseException(e.getMessage());
    }

}
