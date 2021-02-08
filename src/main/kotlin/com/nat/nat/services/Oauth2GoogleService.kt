package com.nat.nat.services

import com.github.scribejava.apis.GoogleApi20
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.oauth.OAuth20Service
import org.springframework.stereotype.Service

@Service("googleService")
class Oauth2GoogleService() : Oauth2Service {

        val googleClientId: String = "365295871686-1jfq9997me458vnspsb2amcrp04jtgq6.apps.googleusercontent.com"

        val googelClientSecret: String = "z3NSgED2bH8ft8ofeCtULfEL"

        val service: OAuth20Service = ServiceBuilder(googleClientId)
                .apiSecret(googelClientSecret)
                .callback("http://localhost:8080/tokenRegistration")
                .defaultScope("profile")
                .build(GoogleApi20.instance())


    override fun getUrl(): String? {
        return service.getAuthorizationUrl()
    }

    override fun getToken(code: String): OAuth2AccessToken? {
        return service.getAccessToken(code)
    }

}