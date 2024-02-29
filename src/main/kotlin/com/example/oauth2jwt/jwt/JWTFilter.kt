package com.example.oauth2jwt.jwt

import com.example.oauth2jwt.dto.CustomOAuth2User
import com.example.oauth2jwt.dto.UserDTO
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTFilter(
    private val jwtUtil: JWTUtil
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authorization: String? = null
        request.cookies?.forEach {
            if (it.name == "Authorization") {
                authorization = it.value
            }
        }

        if (authorization == null) {
            println("token is null")
            return filterChain.doFilter(request, response)
        }

        val token = authorization

        if (jwtUtil.isExpired(token)) {
            println("token is expired")
            return filterChain.doFilter(request, response)
        }

        val username: String = jwtUtil.getUsername(token)
        val role = jwtUtil.getRole(token)

        val userDto = UserDTO(
            role = role,
            name = "",
            username = username
        )

        val customOAuth2User = CustomOAuth2User(userDto)

        val authToken: Authentication = UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
        SecurityContextHolder.getContext().authentication = authToken

        return filterChain.doFilter(request, response)

    }
}