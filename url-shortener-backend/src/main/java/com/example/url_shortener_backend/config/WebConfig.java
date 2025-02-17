package com.example.url_shortener_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS) settings.
 * Implements {@link WebMvcConfigurer} to customize Spring MVC configuration.
 * <p>
 * This configuration is necessary to allow the frontend application to communicate
 * with the backend API by setting up appropriate CORS policies.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for the application endpoints.
     * <p>
     * Sets up the following CORS configurations:
     * <ul>
     *     <li>Applies to all endpoints ("/**")</li>
     *     <li>Allows requests from Angular development server</li>
     *     <li>Permits specific HTTP methods</li>
     *     <li>Allows all request headers</li>
     * </ul>
     * </p>
     *
     * @param registry the {@link CorsRegistry} to configure CORS settings
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "https://stellar-url.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(true)
                .maxAge(3600);
    }
} 