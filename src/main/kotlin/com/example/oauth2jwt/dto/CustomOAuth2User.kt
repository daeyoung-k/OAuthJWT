package com.example.oauth2jwt.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val userDTO: UserDTO
): OAuth2User {

    override fun getName(): String {
        return userDTO.name
    }

    override fun <A : Any?> getAttribute(name: String?): A? {
        return super.getAttribute(name)
    }

    override fun getAttributes(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val collection: MutableCollection<GrantedAuthority> = mutableListOf()
        collection.add(GrantedAuthority{ userDTO.role })
        return collection
    }

    fun getUsername(): String {
        return userDTO.username
    }
}