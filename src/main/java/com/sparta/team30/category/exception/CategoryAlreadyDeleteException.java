package com.sparta.team30.category.exception;

public class CategoryAlreadyDeleteException extends RuntimeException {
    public CategoryAlreadyDeleteException(String message) {
        super(message);
    }
}
