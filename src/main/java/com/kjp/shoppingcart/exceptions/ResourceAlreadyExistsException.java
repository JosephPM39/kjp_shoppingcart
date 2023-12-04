package com.kjp.shoppingcart.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
