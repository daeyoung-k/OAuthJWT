package com.example.oauth2jwt.config

import com.example.oauth2jwt.jwt.JWTFilter
import com.example.oauth2jwt.jwt.JWTUtil
import com.example.oauth2jwt.oauth2.CustomSuccessHandler
import com.example.oauth2jwt.service.CustomOAuth2UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.tomcat.util.file.ConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customSuccessHandler: CustomSuccessHandler,
    private val jwtUtil: JWTUtil
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .anonymous{ it.disable() }
            .oauth2Login{it ->
                it.userInfoEndpoint { it.userService(customOAuth2UserService) }
                it.successHandler(customSuccessHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/**",
                ).permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                JWTFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("localhost:3000")
        configuration.allowedMethods = listOf("GET", "POST")
        configuration.allowedHeaders = listOf("*")
        configuration.maxAge = 3600L
        configuration.exposedHeaders = listOf("Authorization")
        configuration.exposedHeaders = listOf("Set-Cookie")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}