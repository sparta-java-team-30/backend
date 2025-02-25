package com.sparta.team30.store.exception;

public class StoreAlreadyDeleteException extends RuntimeException {
    public StoreAlreadyDeleteException(String message) {
        super(message);
    }
}