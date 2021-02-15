package com.nat.nat.services

import com.github.scribejava.apis.GoogleApi20
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.oauth.OAuth20Service
import com.nat.nat.apis.SpotifyApi20
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service("spotifyOauthService")
class Oauth2SpotifyService(@Autowired
                           final val env: Environment?) : Oauth2Service {
    private val spotifyClientId: String? = this.env?.getProperty("spotify.client.id")
    private val spotifyClientSecret: String? = this.env?.getProperty("spotify.client.secret")


    val service: OAuth20Service = ServiceBuilder(spotifyClientId)
            .apiSecret(spotifyClientSecret)
            .callback("http://localhost:8080/services?from=spotify")
            .defaultScope("user-read-private, user-library-read, user-library-modify")
            .build(SpotifyApi20.instance())

    override fun getUrl(): String? {
        return service.authorizationUrl
    }

    override fun getToken(code: String): OAuth2AccessToken? {
        return service.getAccessToken(code)
    }
}