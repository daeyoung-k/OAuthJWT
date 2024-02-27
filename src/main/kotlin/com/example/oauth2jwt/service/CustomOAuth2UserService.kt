package com.example.oauth2jwt.service

import com.example.oauth2jwt.dto.*
import com.example.oauth2jwt.entity.UserEntity
import com.example.oauth2jwt.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User? {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User: OAuth2User = delegate.loadUser(userRequest)
        println("oAuth2User: $oAuth2User")

        val registrationId: String = userRequest.clientRegistration.registrationId

        val oAuth2Response: OAuth2Response = when (registrationId) {
            "google" -> {
                GoogleResponse(oAuth2User.attributes as MutableMap<String, Any>)
            }

            "naver" -> {
                NaverResponse(oAuth2User.attributes as MutableMap<String, Any>)
            }

            else -> return null
        }

        val userName = "${oAuth2Response.getProvider()}${oAuth2Response.getProviderId()}"

        var existData: UserEntity? = userRepository.findByUsername(userName)

        if (existData == null) {
            val userEntity = UserEntity(
                username = userName,
                name = oAuth2Response.getName(),
                email = oAuth2Response.getEmail(),
                role = "ROLE_USER"
            )
            userRepository.save(userEntity)

            val userDTO = UserDTO(
                username = userName,
                name = oAuth2Response.getName(),
                role = "ROLE_USER"
            )
            return CustomOAuth2User(userDTO)
        } else {

            existData.email = oAuth2Response.getEmail()
            existData.name = oAuth2Response.getName()

            userRepository.save(existData)

            val userDTO = UserDTO(
                username = existData.username,
                name = existData.name,
                role = existData.role
            )
            return CustomOAuth2User(userDTO)
        }

    }
}