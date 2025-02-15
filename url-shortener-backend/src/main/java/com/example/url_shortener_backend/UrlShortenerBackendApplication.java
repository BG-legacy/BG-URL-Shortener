package com.example.url_shortener_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the URL Shortener Backend application.
 * <p>
 * This Spring Boot application provides REST APIs for:
 * <ul>
 *     <li>URL shortening</li>
 *     <li>URL redirection</li>
 *     <li>URL statistics tracking</li>
 * </ul>
 * The application uses:
 * <ul>
 *     <li>MongoDB for data persistence</li>
 *     <li>Spring Web MVC for REST endpoints</li>
 *     <li>Spring Boot for application configuration</li>
 * </ul>
 * </p>
 *
 * @author Your Name
 * @version 1.0
 */
@SpringBootApplication
public class UrlShortenerBackendApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerBackendApplication.class, args);
	}

}
