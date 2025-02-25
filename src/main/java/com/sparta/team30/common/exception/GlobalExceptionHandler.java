package com.sparta.team30.common.exception;

import com.sparta.team30.category.exception.CategoryAlreadyDeleteException;
import com.sparta.team30.category.exception.CategoryNotFoundException;
import com.sparta.team30.category.exception.DuplicateCategoryException;
import com.sparta.team30.review.exception.ReviewAccessDeniedException;
import com.sparta.team30.review.exception.ReviewNotFoundException;
import com.sparta.team30.review.exception.ReviewTimeExpiredException;
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
    public ResponseEntity<ErrorResponse> AddressAccessDeniedException(AddressAccessDeniedException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_ACCEPTABLE
        );
    }

    @ExceptionHandler(AlreadyPaidException.class)
    public ResponseEntity<ErrorResponse> AlreadyPaidException(AlreadyPaidException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> PaymentNotFoundException(PaymentNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ErrorResponse> storeNotFoundException(StoreNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateStoreException.class)
    public ResponseEntity<ErrorResponse> duplicateStoreException(DuplicateStoreException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> reviewNotFoundException(ReviewNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ReviewAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> reviewAccessDeniedException(ReviewAccessDeniedException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ReviewTimeExpiredException.class)
    public ResponseEntity<ErrorResponse> reviewTimeExpiredException(ReviewTimeExpiredException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.GONE.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.GONE
        );
    }

    @ExceptionHandler(CategoryAlreadyDeleteException.class)
    public ResponseEntity<ErrorResponse> categoryAlreadyDeleteException(CategoryAlreadyDeleteException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.GONE.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.GONE
        );
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ErrorResponse> duplicateCategoryException(DuplicateCategoryException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }
}
