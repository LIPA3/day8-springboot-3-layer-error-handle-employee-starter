package com.example.demo.advice;

import com.example.demo.Exception.IllegalEmployeeException;
import com.example.demo.Exception.InvalidAgeException;
import com.example.demo.Exception.InvalidSalaryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalEmployeeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException handleResponseStatusException(IllegalEmployeeException e) {
        return new ResponseException( e.getMessage());
    }
    @ExceptionHandler(InvalidAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleInvalidAgeException(InvalidAgeException e) {
        return new ResponseException(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handlerArgumentNotValid(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        return new ErrorResponse(errorMessage);
    }
    @ExceptionHandler(InvalidSalaryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleInvalidSalaryException(InvalidSalaryException e) {
        return new ResponseException(e.getMessage());
    }
}
