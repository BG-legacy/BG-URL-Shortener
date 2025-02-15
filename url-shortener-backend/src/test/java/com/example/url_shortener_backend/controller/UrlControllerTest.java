package com.example.url_shortener_backend.controller;

import com.example.url_shortener_backend.dto.UrlDto;
import com.example.url_shortener_backend.model.Url;
import com.example.url_shortener_backend.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link UrlController} class.
 * <p>
 * Tests the REST endpoints for URL shortening operations using Spring's MockMvc.
 * These tests verify:
 * <ul>
 *     <li>URL shortening functionality</li>
 *     <li>URL redirection</li>
 *     <li>Statistics retrieval</li>
 *     <li>Error handling</li>
 * </ul>
 * </p>
 */
@WebMvcTest(UrlController.class)
@ExtendWith(SpringExtension.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UrlService urlService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ORIGINAL_URL = "https://www.example.com";
    private static final String SHORT_ID = "abc123";

    /**
     * Tests successful URL shortening operation.
     * <p>
     * Verifies that:
     * <ul>
     *     <li>The endpoint returns 201 Created status</li>
     *     <li>The response contains the correct original URL</li>
     *     <li>The response contains the properly formatted short URL</li>
     * </ul>
     * </p>
     */
    @Test
    void shortenUrl_Success() throws Exception {
        // Arrange
        UrlDto urlDto = new UrlDto();
        urlDto.setUrl(ORIGINAL_URL);

        Url url = new Url();
        url.setOriginalUrl(ORIGINAL_URL);
        url.setShortId(SHORT_ID);
        url.setCreatedAt(LocalDateTime.now());
        url.setClickCount(0);

        when(urlService.createShortUrl(ORIGINAL_URL, null)).thenReturn(url);

        // Act & Assert
        mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(urlDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value(ORIGINAL_URL))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/" + SHORT_ID));
    }

    /**
     * Tests URL shortening with invalid URL format.
     * <p>
     * Verifies that the endpoint returns 400 Bad Request status
     * when an invalid URL format is provided.
     * </p>
     */
    @Test
    void shortenUrl_InvalidUrl() throws Exception {
        // Arrange
        UrlDto urlDto = new UrlDto();
        urlDto.setUrl("invalid-url");

        // Act & Assert
        mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(urlDto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests successful retrieval of URL statistics.
     * <p>
     * Verifies that:
     * <ul>
     *     <li>The endpoint returns 200 OK status</li>
     *     <li>The response contains the correct original URL</li>
     *     <li>The response contains the correct click count</li>
     * </ul>
     * </p>
     */
    @Test
    void getStats_Success() throws Exception {
        // Arrange
        Url url = new Url();
        url.setOriginalUrl(ORIGINAL_URL);
        url.setShortId(SHORT_ID);
        url.setCreatedAt(LocalDateTime.now());
        url.setClickCount(5);

        when(urlService.getUrlByShortId(SHORT_ID)).thenReturn(url);

        // Act & Assert
        mockMvc.perform(get("/stats/" + SHORT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(ORIGINAL_URL))
                .andExpect(jsonPath("$.clickCount").value(5));
    }

    /**
     * Tests successful URL redirection.
     * <p>
     * Verifies that:
     * <ul>
     *     <li>The endpoint returns 3xx Redirection status</li>
     *     <li>The response contains the correct redirect URL</li>
     *     <li>The click count is incremented</li>
     * </ul>
     * </p>
     */
    @Test
    void redirect_Success() throws Exception {
        // Arrange
        Url url = new Url();
        url.setOriginalUrl(ORIGINAL_URL);
        url.setShortId(SHORT_ID);

        when(urlService.incrementClickCount(anyString())).thenReturn(url);

        // Act & Assert
        mockMvc.perform(get("/" + SHORT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ORIGINAL_URL));
    }
} 