package com.sparta.team30.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ErrorResponse> nullPointerException(NullPointerException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({UsernameAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> usernameAlreadyExistsException (UsernameAlreadyExistsException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({UserEmailAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> userEmailAlreadyExistsException (UserEmailAlreadyExistsException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({UserPasswordIncorrectException.class})
    public ResponseEntity<ErrorResponse> userPasswordIncorrectException (UserPasswordIncorrectException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }
}
