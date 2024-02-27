package com.example.oauth2jwt.dto

import java.util.*

class GoogleResponse(
    private val attribute: MutableMap<String, Any>
): OAuth2Response {

    override fun getProvider(): String {
        return "google"
    }

    override fun getProviderId(): String {
        return attribute["sub"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }

    override fun getName(): String {
        return attribute["name"].toString()
    }
}