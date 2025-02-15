package com.example.url_shortener_backend.service;

import com.example.url_shortener_backend.model.Url;
import com.example.url_shortener_backend.repository.UrlRepository;
import com.example.url_shortener_backend.exception.UrlNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class handling URL shortening business logic.
 * <p>
 * This service provides functionality for:
 * <ul>
 *     <li>Creating shortened URLs</li>
 *     <li>Retrieving URL information</li>
 *     <li>Tracking URL usage statistics</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    
    /** Characters used for generating random short IDs */
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    /** Length of generated short IDs */
    private static final int SHORT_ID_LENGTH = 6;
    
    private static final Logger log = LoggerFactory.getLogger(UrlService.class);
    
    /**
     * Creates a new shortened URL.
     * <p>
     * If a custom alias is provided, it will be used as the short ID after checking
     * for uniqueness. Otherwise, a random short ID will be generated.
     * </p>
     *
     * @param originalUrl the URL to be shortened
     * @param customAlias optional custom alias for the shortened URL
     * @return the created {@link Url} entity
     * @throws IllegalArgumentException if the custom alias is already in use
     */
    public Url createShortUrl(String originalUrl, String customAlias) {
        String shortId;
        
        if (customAlias != null && !customAlias.isEmpty()) {
            if (urlRepository.existsByShortId(customAlias)) {
                throw new IllegalArgumentException("Custom alias already exists");
            }
            shortId = customAlias;
        } else {
            shortId = generateUniqueShortId();
        }
        
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortId(shortId);
        url.setCreatedAt(LocalDateTime.now());
        url.setClickCount(0);
        
        return urlRepository.save(url);
    }
    
    /**
     * Retrieves a URL entity by its short identifier.
     *
     * @param shortId the short identifier of the URL
     * @return the {@link Url} entity if found
     * @throws UrlNotFoundException if no URL is found with the given short ID
     */
    public Url getUrlByShortId(String shortId) {
        return urlRepository.findByShortId(shortId)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for id: " + shortId));
    }
    
    /**
     * Increments the click count for a URL and updates its last accessed timestamp.
     * <p>
     * This method is called when a shortened URL is accessed for redirection.
     * </p>
     *
     * @param shortId the short identifier of the URL
     * @return the updated {@link Url} entity
     * @throws UrlNotFoundException if no URL is found with the given short ID
     */
    public Url incrementClickCount(String shortId) {
        log.info("Incrementing click count for shortId: {}", shortId);
        Url url = getUrlByShortId(shortId);
        log.info("Current click count: {}", url.getClickCount());
        
        url.setClickCount(url.getClickCount() + 1);
        url.onUpdate();
        
        Url savedUrl = urlRepository.save(url);
        log.info("New click count after save: {}", savedUrl.getClickCount());
        return savedUrl;
    }
    
    /**
     * Generates a unique random short identifier.
     * <p>
     * The generated ID:
     * <ul>
     *     <li>Is {@value #SHORT_ID_LENGTH} characters long</li>
     *     <li>Contains only alphanumeric characters</li>
     *     <li>Is guaranteed to be unique in the database</li>
     * </ul>
     * </p>
     *
     * @return a unique short identifier
     */
    private String generateUniqueShortId() {
        Random random = new Random();
        String shortId;
        do {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < SHORT_ID_LENGTH; i++) {
                builder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            shortId = builder.toString();
        } while (urlRepository.existsByShortId(shortId));
        
        return shortId;
    }
} 