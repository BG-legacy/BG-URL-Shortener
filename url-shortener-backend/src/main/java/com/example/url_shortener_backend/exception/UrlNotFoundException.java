package com.example.url_shortener_backend.exception;

/**
 * Exception thrown when a requested URL short ID cannot be found in the system.
 * <p>
 * This runtime exception is used when:
 * <ul>
 *     <li>Attempting to redirect to a non-existent short URL</li>
 *     <li>Requesting statistics for a non-existent short URL</li>
 * </ul>
 * </p>
 */
public class UrlNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new UrlNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining why the URL was not found
     */
    public UrlNotFoundException(String message) {
        super(message);
    }
} 