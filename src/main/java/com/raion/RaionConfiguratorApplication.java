package com.raion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Main Spring Boot application class
// This starts up the entire backend server
@SpringBootApplication
public class RaionConfiguratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaionConfiguratorApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Raion EV Configurator API is running!");
        System.out.println("API available at: http://localhost:8080");
        System.out.println("========================================\n");
    }

    // Enable CORS so the frontend can call our API
    // This allows requests from the frontend running on a different port
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

