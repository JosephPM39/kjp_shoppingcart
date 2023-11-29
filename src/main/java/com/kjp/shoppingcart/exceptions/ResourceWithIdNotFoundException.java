package com.kjp.shoppingcart.exceptions;

public class ResourceWithIdNotFoundException extends RuntimeException {
    public ResourceWithIdNotFoundException(String message) {
        super(message);
    }
}
