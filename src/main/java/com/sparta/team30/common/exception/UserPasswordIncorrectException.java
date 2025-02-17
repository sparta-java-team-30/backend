package com.sparta.team30.common.exception;

public class UserPasswordIncorrectException extends RuntimeException{
    public UserPasswordIncorrectException(String message) {
        super(message);
    }
}
