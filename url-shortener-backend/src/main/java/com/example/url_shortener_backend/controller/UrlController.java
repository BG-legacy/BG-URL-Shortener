package com.example.url_shortener_backend.controller;

import com.example.url_shortener_backend.dto.UrlDto;
import com.example.url_shortener_backend.dto.UrlResponseDto;
import com.example.url_shortener_backend.model.Url;
import com.example.url_shortener_backend.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST Controller for handling URL shortening operations.
 * <p>
 * This controller provides endpoints for:
 * <ul>
 *     <li>Creating shortened URLs</li>
 *     <li>Redirecting to original URLs</li>
 *     <li>Retrieving URL statistics</li>
 * </ul>
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UrlController {

    private final UrlService urlService;
    
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Creates a shortened URL from the provided original URL.
     * <p>
     * Accepts a URL and optional custom alias, generates a short URL, and returns
     * the details including the shortened URL and creation timestamp.
     * </p>
     *
     * @param urlDto the DTO containing the original URL and optional custom alias
     * @return ResponseEntity containing the created URL details
     * @throws ResponseStatusException with HTTP 409 if custom alias is already in use
     */
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDto> createShortUrl(@Valid @RequestBody UrlDto urlDto) {
        log.info("Received URL shortening request for: {}", urlDto.getUrl());
        try {
            if (urlDto.getUrl() == null || urlDto.getUrl().trim().isEmpty()) {
                throw new IllegalArgumentException("URL cannot be empty");
            }

            Url url = urlService.createShortUrl(urlDto.getUrl(), urlDto.getCustomAlias());
            
            UrlResponseDto response = new UrlResponseDto();
            response.setOriginalUrl(url.getOriginalUrl());
            response.setShortUrl(baseUrl + url.getShortId());
            response.setCreatedAt(url.getCreatedAt());
            response.setClickCount(url.getClickCount());
            
            log.info("Created short URL: {}", response.getShortUrl());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating short URL", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating short URL: " + e.getMessage());
        }
    }

    /**
     * Redirects to the original URL associated with the provided short ID.
     * <p>
     * Increments the click count for the URL and performs a redirect to the original URL.
     * </p>
     *
     * @param shortId the short identifier for the URL
     * @return ResponseEntity with redirect headers to the original URL
     * @throws ResponseStatusException with HTTP 404 if the short ID is not found
     */
    @GetMapping("/{shortId}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortId) {
        log.info("Received redirect request for shortId: {}", shortId);
        
        // Get and update URL
        Url url = urlService.incrementClickCount(shortId);
        log.info("Redirecting to: {} with click count: {}", url.getOriginalUrl(), url.getClickCount());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.getOriginalUrl()));
        
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Retrieves statistics for a shortened URL.
     * <p>
     * Returns details about the URL including:
     * <ul>
     *     <li>Original URL</li>
     *     <li>Shortened URL</li>
     *     <li>Creation timestamp</li>
     *     <li>Click count</li>
     * </ul>
     * </p>
     *
     * @param shortId the short identifier for the URL
     * @return ResponseEntity containing the URL statistics
     * @throws ResponseStatusException with HTTP 404 if the short ID is not found
     */
    @GetMapping("/stats/{shortId}")
    public ResponseEntity<UrlResponseDto> getUrlStats(@PathVariable String shortId) {
        log.info("Received request for URL stats with id: {}", shortId);
        Url url = urlService.getUrlByShortId(shortId);
        
        UrlResponseDto response = new UrlResponseDto();
        response.setOriginalUrl(url.getOriginalUrl());
        response.setShortUrl(baseUrl + url.getShortId());
        response.setCreatedAt(url.getCreatedAt());
        response.setClickCount(url.getClickCount());
        
        return ResponseEntity.ok(response);
    }
} 