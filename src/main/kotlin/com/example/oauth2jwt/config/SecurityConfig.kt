package com.example.oauth2jwt.config

import com.example.oauth2jwt.oauth2.CustomSuccessHandler
import com.example.oauth2jwt.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customSuccessHandler: CustomSuccessHandler
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
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

        return http.build()
    }
}