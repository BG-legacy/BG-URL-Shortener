package com.example.url_shortener_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * MongoDB document model representing a shortened URL.
 * <p>
 * This class stores all information related to a shortened URL, including:
 * <ul>
 *     <li>The original URL</li>
 *     <li>The shortened identifier</li>
 *     <li>Usage statistics</li>
 *     <li>Timestamps for creation and access</li>
 * </ul>
 * </p>
 */
@Data
@Document(collection = "urls")
public class Url {
    /**
     * MongoDB document identifier
     */
    @Id
    private String id;
    
    /**
     * The original URL that was shortened.
     * Must be a valid URL format and cannot be empty.
     */
    @NotBlank(message = "Original URL cannot be empty")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", message = "Invalid URL format")
    private String originalUrl;
    
    /**
     * The shortened identifier for the URL.
     * Must be unique across all documents.
     */
    @NotBlank(message = "Short ID cannot be empty")
    @Indexed(unique = true)
    private String shortId;
    
    /**
     * Timestamp when the shortened URL was created
     */
    private LocalDateTime createdAt;
    
    /**
     * Number of times the shortened URL has been accessed.
     * Cannot be negative.
     */
    @Min(value = 0, message = "Click count cannot be negative")
    private int clickCount;
    
    /**
     * Timestamp of the last access to this shortened URL
     */
    @LastModifiedDate
    private LocalDateTime lastAccessedAt;
    
    /**
     * Flag indicating if the shortened URL is active and can be accessed
     */
    private boolean active = true;
    
    /**
     * Lifecycle method called when creating a new URL document.
     * Initializes creation and last accessed timestamps.
     */
    public void onCreate() {
        createdAt = LocalDateTime.now();
        lastAccessedAt = LocalDateTime.now();
    }
    
    /**
     * Lifecycle method called when updating the URL document.
     * Updates the last accessed timestamp.
     */
    public void onUpdate() {
        lastAccessedAt = LocalDateTime.now();
    }
} 