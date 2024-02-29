package com.example.oauth2jwt.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtil(
) {

    @Value("\${jwt.secret-key}")
    lateinit var secretKey: String
    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    private fun getClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    fun getUsername(token: String?): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload["username"].toString()
    }

    fun getRole(token: String?): String {
//        return Jwts.parser()
//            .verifyWith(key)
//            .build()
//            .parseSignedClaims(token)
//            .payload["role"].toString()

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get<String>(
            "role",
            String::class.java
        )
    }

    fun isExpired(token: String?): Boolean {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload.expiration.before(Date())

//        return Jwts.parser()
//            .verifyWith(key)
//            .build()
//            .parseSignedClaims(token)
//            .getPayload()
//            .getExpiration()
//            .before(Date())
    }

    fun createJwt(
        username: String,
        role: String,
        expiredMs: Long): String {

        return Jwts.builder()
            .claim("username", username)
            .claim("role", role)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiredMs))
            .signWith(key)
            .compact()
    }
}