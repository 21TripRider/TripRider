package com.TripRider.TripRider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String legacy = System.getProperty("user.home") + "/triprider-uploads/";
        String current = System.getProperty("user.home") + "/TripRider/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:///" + current,   // 새 경로(우선 탐색)
                        "file:///" + legacy    // 예전 경로(백업)
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 또는 ngrok 주소로 제한
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }



}