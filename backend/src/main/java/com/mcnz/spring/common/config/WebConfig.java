package com.mcnz.spring.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // This means all endpoints will accept CORS requests
                .allowedOrigins("http://localhost:5173") // React's development URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // You can specify allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // If you're using cookies or other credentials
    }
}