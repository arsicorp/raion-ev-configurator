package com.raion;

import com.raion.services.ReceiptGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * main spring boot application class
 * this starts up the entire raion ev configurator backend server
 *
 * features:
 * - rest api for vehicle configuration and ordering
 * - cors enabled for frontend access
 * - automatic receipts folder creation
 */
@SpringBootApplication
public class RaionConfiguratorApplication {

    public static void main(String[] args) {
        // start spring boot application
        SpringApplication.run(RaionConfiguratorApplication.class, args);

        // print startup information
        printStartupInfo();

        // print receipts folder information
        ReceiptGenerator.printReceiptsFolderInfo();
    }

    /**
     * enable cors (cross-origin resource sharing)
     * this allows the frontend to call our api from any origin
     *
     * for production, you should restrict allowedOrigins to specific domains
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // allow all origins for development (file://, localhost on any port, etc.)
                        .allowedOrigins("*")
                        .allowedOriginPatterns("*")
                        // allow all http methods
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        // allow all headers
                        .allowedHeaders("*")
                        // credentials must be false when allowedOrigins is "*"
                        .allowCredentials(false)
                        // cache preflight response for 1 hour
                        .maxAge(3600);
            }
        };
    }

    /**
     * print startup information to console
     */
    private static void printStartupInfo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("RAION MOTORS - EV Configurator API");
        System.out.println("=".repeat(60));
        System.out.println("Server Status: RUNNING");
        System.out.println("API Base URL: http://localhost:8080/api");
        System.out.println("CORS Policy: Enabled (All origins allowed)");
        System.out.println("\nAvailable Endpoints:");
        System.out.println("  GET  /api/vehicles        - Get all vehicle models");
        System.out.println("  GET  /api/vehicles/{id}   - Get vehicle configuration data");
        System.out.println("  GET  /api/signatures      - Get all signature vehicles");
        System.out.println("  POST /api/order           - Place custom vehicle order");
        System.out.println("  POST /api/order/signature - Place signature vehicle order");
        System.out.println("\nConfiguration:");
        System.out.println("  Working Directory: " + System.getProperty("user.dir"));
        System.out.println("  Java Version: " + System.getProperty("java.version"));
        System.out.println("=".repeat(60) + "\n");
    }
}