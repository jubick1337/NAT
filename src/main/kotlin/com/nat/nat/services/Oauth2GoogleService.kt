package com.nat.nat.services

import com.github.scribejava.apis.GoogleApi20
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.oauth.OAuth20Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service("googleService")
class Oauth2GoogleService(@Autowired
                          final val env: Environment?
) : Oauth2Service {

    private val googleClientId: String? = this.env?.getProperty("google.client.id")
    private val googleClientSecret: String? = this.env?.getProperty("google.client.secret")

    val service: OAuth20Service = ServiceBuilder(googleClientId)
            .apiSecret(googleClientSecret)
            .callback("http://localhost:8080/services")
            .defaultScope("profile")
            .build(GoogleApi20.instance())


    override fun getUrl(): String? {
        return service.getAuthorizationUrl()
    }

    override fun getToken(code: String): OAuth2AccessToken? {
        return service.getAccessToken(code)
    }

}