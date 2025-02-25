package com.sparta.team30.review.exception;

public class ReviewTimeExpiredException extends RuntimeException {
    public ReviewTimeExpiredException(String message) {
        super(message);
    }
}