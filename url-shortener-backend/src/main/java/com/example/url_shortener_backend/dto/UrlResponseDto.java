package com.example.url_shortener_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning URL shortening results and statistics.
 * <p>
 * This class contains all relevant information about a shortened URL,
 * including the original URL, shortened version, creation time, and usage statistics.
 * </p>
 */
@Data
public class UrlResponseDto {
    /**
     * The original URL that was shortened
     */
    private String originalUrl;

    /**
     * The shortened URL (including base URL and short ID)
     */
    private String shortUrl;

    /**
     * Timestamp when the shortened URL was created
     */
    private LocalDateTime createdAt;

    /**
     * Number of times the shortened URL has been accessed
     */
    private int clickCount;
} 