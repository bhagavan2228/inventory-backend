package com.inventory.inventory_management_system.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.inventory.inventory_management_system.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 404 — Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(404, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔴 400 — Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return new ResponseEntity<>(
                new ErrorResponse(400, message),
                HttpStatus.BAD_REQUEST
        );
    }

    // 🔴 400 — Business rule violation
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleStock(InsufficientStockException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(400, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // 🔴 500 — Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(500, "Internal server error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
