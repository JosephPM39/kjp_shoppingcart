package com.kjp.shoppingcart.advice;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class AuthExceptionHandler {

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<Object> handleRestClientException(HttpClientErrorException ex) {
    HttpStatusCode status = ex.getStatusCode();
    String message = ex.getResponseBodyAsString();
    if (status.value() != 401) {
      status = HttpStatusCode.valueOf(500);
      message = "Internal Server Error";
    }
    return ResponseEntity.status(status).body(message);
  }
}
