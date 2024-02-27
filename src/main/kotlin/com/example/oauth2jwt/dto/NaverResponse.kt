package com.example.oauth2jwt.dto

import java.util.Objects

class NaverResponse(
   private val attribute: MutableMap<String, Any>
): OAuth2Response {

    override fun getProvider(): String {
        return "naver"
    }

    override fun getProviderId(): String {
        return attribute["id"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }

    override fun getName(): String {
        return attribute["name"].toString()
    }
}