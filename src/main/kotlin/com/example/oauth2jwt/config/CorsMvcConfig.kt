package com.example.oauth2jwt.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsMvcConfig: WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .exposedHeaders("Set-Cookie")
            .allowedOrigins(
                "http://localhost:3000"
            )
    }
}