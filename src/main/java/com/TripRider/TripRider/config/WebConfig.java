package com.TripRider.TripRider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = System.getProperty("user.home") + "/triprider-uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 또는 ngrok 주소로 제한
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }


}