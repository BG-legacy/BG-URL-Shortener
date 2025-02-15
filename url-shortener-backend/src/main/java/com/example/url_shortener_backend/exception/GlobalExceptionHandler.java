package com.example.url_shortener_backend.exception;

import com.example.url_shortener_backend.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler for the URL shortener application.
 * <p>
 * This class provides centralized exception handling across all controllers
 * and converts various exceptions into standardized error responses.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions when a requested URL is not found.
     *
     * @param ex The UrlNotFoundException that was thrown
     * @param request The HTTP request that triggered the exception
     * @return ResponseEntity containing error details with HTTP 404 status
     */
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUrlNotFoundException(
            UrlNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDto error = new ErrorResponseDto(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation exceptions for invalid request parameters or body.
     * <p>
     * Processes field validation errors and returns the first validation
     * error message found.
     * </p>
     *
     * @param ex The validation exception that was thrown
     * @param request The HTTP request that triggered the exception
     * @return ResponseEntity containing error details with HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ErrorResponseDto error = new ErrorResponseDto(
            LocalDateTime.now(),
            errorMessage,
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general HTTP status exceptions.
     * <p>
     * Processes exceptions with specific HTTP status codes and their
     * associated error messages.
     * </p>
     *
     * @param ex The ResponseStatusException that was thrown
     * @param request The HTTP request that triggered the exception
     * @return ResponseEntity containing error details with the exception's HTTP status
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        ErrorResponseDto error = new ErrorResponseDto(
            LocalDateTime.now(),
            ex.getReason(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
} 