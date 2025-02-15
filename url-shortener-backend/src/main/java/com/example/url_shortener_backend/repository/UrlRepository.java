package com.example.url_shortener_backend.repository;

import com.example.url_shortener_backend.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * MongoDB repository interface for {@link Url} documents.
 * <p>
 * This repository provides CRUD operations for URL documents and extends
 * {@link MongoRepository} to inherit common MongoDB operations.
 * </p>
 */
public interface UrlRepository extends MongoRepository<Url, String> {
    
    /**
     * Finds a URL document by its short identifier.
     * <p>
     * This method is used when:
     * <ul>
     *     <li>Redirecting to the original URL</li>
     *     <li>Retrieving URL statistics</li>
     * </ul>
     * </p>
     *
     * @param shortId the short identifier of the URL
     * @return an Optional containing the URL if found, empty otherwise
     */
    Optional<Url> findByShortId(String shortId);
    
    /**
     * Checks if a URL document with the given short identifier exists.
     * <p>
     * This method is used to:
     * <ul>
     *     <li>Validate custom aliases during URL creation</li>
     *     <li>Ensure uniqueness of generated short IDs</li>
     * </ul>
     * </p>
     *
     * @param shortId the short identifier to check
     * @return true if a URL with the given short ID exists, false otherwise
     */
    boolean existsByShortId(String shortId);
} 