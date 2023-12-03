package com.kjp.shoppingcart.exceptions;

public class BadProductCartQuantityException extends RuntimeException {
    public BadProductCartQuantityException(String message) {
        super(message);
    }
}
