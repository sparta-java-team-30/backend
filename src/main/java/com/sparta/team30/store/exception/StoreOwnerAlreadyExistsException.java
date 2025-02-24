package com.sparta.team30.store.exception;

public class StoreOwnerAlreadyExistsException extends RuntimeException {
    public StoreOwnerAlreadyExistsException(String message) {
        super(message);
    }
}
