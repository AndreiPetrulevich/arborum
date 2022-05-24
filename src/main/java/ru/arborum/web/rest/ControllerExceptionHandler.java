package ru.arborum.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

//TODO REST exception

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException e) {

        List<String> errors = new ArrayList<>();

        e.getBindingResult().getAllErrors().stream()
                .map(FieldError.class::cast)
                .forEach(fieldError -> {
                    errors.add(String.format("Bad request %s %s - your value is %s",
                            fieldError.getDefaultMessage(),
                            fieldError.getField(),
                            fieldError.getRejectedValue()));
                });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
