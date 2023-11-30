package com.kjp.shoppingcart.advice;

import com.kjp.shoppingcart.exceptions.ResourceWithIdNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DataExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Error de violación de integridad de datos";
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceWithIdNotFoundException.class)
    public ResponseEntity<String> handleResourceWithIdNotFound(ResourceWithIdNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            errors.put(error.getPropertyPath().toString(), error.getMessage());
        });
        return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
    }
    
}
