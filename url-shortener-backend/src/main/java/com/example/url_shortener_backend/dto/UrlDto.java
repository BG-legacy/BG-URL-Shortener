package com.example.url_shortener_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Data Transfer Object for receiving URL shortening requests.
 * <p>
 * This class validates the input URL format and optional custom alias
 * through Jakarta validation annotations.
 * </p>
 */
@Data
public class UrlDto {
    /**
     * The original URL to be shortened.
     * <p>
     * Must not be empty and should follow a valid URL format:
     * <ul>
     *     <li>Optional http:// or https:// protocol</li>
     *     <li>Valid domain name with at least one dot</li>
     *     <li>Optional path, query parameters, and fragments</li>
     * </ul>
     * </p>
     */
    @NotBlank(message = "URL cannot be empty")
    @Pattern(regexp = "^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=+#]*)?$", message = "Invalid URL format")
    private String url;
    
    /**
     * Optional custom alias for the shortened URL.
     * <p>
     * When provided, must:
     * <ul>
     *     <li>Be at least 4 characters long</li>
     *     <li>Contain only letters, numbers, hyphens, and underscores</li>
     * </ul>
     * If not provided, a random short ID will be generated.
     * </p>
     */
    @Pattern(regexp = "^[a-zA-Z0-9-_]{4,}$", message = "Custom alias must be at least 4 characters long and can only contain letters, numbers, hyphens, and underscores")
    private String customAlias;
} 