package com.sparta.team30.store.exception;

public class NotStoreOwnerException extends RuntimeException {
    public NotStoreOwnerException(String message) {
        super(message);
    }
}
