package com.kjp.shoppingcart.advice;

import com.kjp.shoppingcart.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.InternalServerErrorException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DataExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolation() {
    return new ResponseEntity<>("Data Integrity Violation", HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceWithIdNotFound(ResourceNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    System.out.println("Errors: " + errors);
    return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleValidation(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(error -> errors.put(error.getPropertyPath().toString(), error.getMessage()));
    System.out.println("Errors: " + errors);
    return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadStrategySearchParams.class)
  public ResponseEntity<String> handleBadStrategySearchParams(BadStrategySearchParams ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadProductCartQuantityException.class)
  public ResponseEntity<String> handleBadProductCartQuantityException(
      BadProductCartQuantityException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<String> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BadStrategyConfigException.class)
  public ResponseEntity<String> handleBadStrategyConfig() {
    return new ResponseEntity<>(
        "Server could not handle search strategy request", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<String> handleInternalServerError() {
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
