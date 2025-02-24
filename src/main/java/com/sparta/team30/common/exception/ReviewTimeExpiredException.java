package com.sparta.team30.common.exception;

public class ReviewTimeExpiredException extends RuntimeException {
    public ReviewTimeExpiredException(String message) {
        super(message);
    }
}