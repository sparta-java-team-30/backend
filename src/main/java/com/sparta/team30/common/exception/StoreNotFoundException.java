package com.sparta.team30.common.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(String message) {
        super(message);
    }
}