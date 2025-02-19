package com.sparta.team30.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<ErrorResponse> usernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({UserEmailAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> userEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({UserPasswordIncorrectException.class})
    public ResponseEntity<ErrorResponse> userPasswordIncorrectException(UserPasswordIncorrectException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(":").append(message).append("\n");
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage.toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(OrderAlreadyProcessedException.class)
    public ResponseEntity<ErrorResponse> OrderAlreadyProcessedException(OrderAlreadyProcessedException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> OrderNotFoundException(OrderNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> OrderAccessDeniedException(OrderAccessDeniedException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_ACCEPTABLE
        );
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> AddressNotFoundException(AddressNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(AddressAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> AddressAccessDeniedException(OrderAccessDeniedException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_ACCEPTABLE
        );
    }
}
