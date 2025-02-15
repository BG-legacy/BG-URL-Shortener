package com.example.url_shortener_backend.service;

import com.example.url_shortener_backend.exception.UrlNotFoundException;
import com.example.url_shortener_backend.model.Url;
import com.example.url_shortener_backend.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UrlService} class.
 * <p>
 * Tests the business logic for URL shortening operations including:
 * <ul>
 *     <li>URL creation and validation</li>
 *     <li>URL retrieval</li>
 *     <li>Click count tracking</li>
 * </ul>
 * Uses Mockito for mocking the repository layer.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    private static final String ORIGINAL_URL = "https://www.example.com";
    private static final String SHORT_ID = "abc123";

    /**
     * Tests successful creation of a shortened URL.
     * <p>
     * Verifies that:
     * <ul>
     *     <li>A unique short ID is generated</li>
     *     <li>The URL is properly saved</li>
     *     <li>Initial click count is zero</li>
     *     <li>All required fields are set</li>
     * </ul>
     * </p>
     */
    @Test
    void createShortUrl_Success() {
        // Arrange
        when(urlRepository.existsByShortId(any())).thenReturn(false);
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> {
            Url url = invocation.getArgument(0);
            url.setId("someId");
            return url;
        });

        // Act
        Url result = urlService.createShortUrl(ORIGINAL_URL, null);

        // Assert
        assertNotNull(result);
        assertEquals(ORIGINAL_URL, result.getOriginalUrl());
        assertNotNull(result.getShortId());
        assertEquals(0, result.getClickCount());
        verify(urlRepository).save(any(Url.class));
    }

    /**
     * Tests successful retrieval of a URL by its short ID.
     * <p>
     * Verifies that:
     * <ul>
     *     <li>The correct URL is retrieved</li>
     *     <li>All URL properties match expected values</li>
     * </ul>
     * </p>
     */
    @Test
    void getUrlByShortId_Success() {
        // Arrange
        Url url = new Url();
        url.setShortId(SHORT_ID);
        url.setOriginalUrl(ORIGINAL_URL);
        when(urlRepository.findByShortId(SHORT_ID)).thenReturn(Optional.of(url));

        // Act
        Url result = urlService.getUrlByShortId(SHORT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(SHORT_ID, result.getShortId());
        assertEquals(ORIGINAL_URL, result.getOriginalUrl());
    }

    /**
     * Tests URL retrieval with a non-existent short ID.
     * <p>
     * Verifies that a {@link UrlNotFoundException} is thrown when
     * attempting to retrieve a URL with an invalid short ID.
     * </p>
     */
    @Test
    void getUrlByShortId_NotFound() {
        // Arrange
        when(urlRepository.findByShortId(SHORT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UrlNotFoundException.class, () -> 
            urlService.getUrlByShortId(SHORT_ID)
        );
    }

    /**
     * Tests successful increment of URL click count.
     * <p>
     * Verifies that:
     * <ul>
     *     <li>Click count is incremented by one</li>
     *     <li>Updated URL is saved to repository</li>
     *     <li>Other URL properties remain unchanged</li>
     * </ul>
     * </p>
     */
    @Test
    void incrementClickCount_Success() {
        // Arrange
        Url url = new Url();
        url.setShortId(SHORT_ID);
        url.setOriginalUrl(ORIGINAL_URL);
        url.setClickCount(0);

        when(urlRepository.findByShortId(SHORT_ID)).thenReturn(Optional.of(url));
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Url result = urlService.incrementClickCount(SHORT_ID);

        // Assert
        assertEquals(1, result.getClickCount());
        verify(urlRepository).save(any(Url.class));
    }
} 