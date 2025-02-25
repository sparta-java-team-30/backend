package com.sparta.team30.store.exception;

public class UserHasNoStoreException extends RuntimeException {
    public UserHasNoStoreException(String message) {
        super(message);
    }
}