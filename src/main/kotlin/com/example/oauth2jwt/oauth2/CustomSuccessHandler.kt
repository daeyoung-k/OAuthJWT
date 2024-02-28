package com.example.oauth2jwt.oauth2

import com.example.oauth2jwt.dto.CustomOAuth2User
import com.example.oauth2jwt.jwt.JWTUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component


@Component
class CustomSuccessHandler(
    private val jwtUtil: JWTUtil,
): SimpleUrlAuthenticationSuccessHandler() {


    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val customOAuth2User: CustomOAuth2User = authentication.principal as CustomOAuth2User

        val username: String = customOAuth2User.getUsername()

        val authorities = authentication.authorities
        val iterator: Iterator<GrantedAuthority> = authorities.iterator()
        val auth = iterator.next()
        val role = auth.authority

        val token: String = jwtUtil.createJwt(username, role, 60 * 60 * 60L)


        response.addCookie(createCookie(token))
        response.sendRedirect("http://localhost:3000/")

        super.onAuthenticationSuccess(request, response, authentication)
    }

    private fun createCookie(
        value: String
    ): Cookie {
        val cookie = Cookie("Authorization", value)
        cookie.maxAge = 60*60*60
        cookie.path = "/"
        cookie.isHttpOnly = true

        return cookie

    }
}