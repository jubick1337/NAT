package com.nat.nat.apis

import com.github.scribejava.apis.GoogleApi20
import com.github.scribejava.apis.google.GoogleDeviceAuthorizationJsonExtractor
import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.extractors.DeviceAuthorizationJsonExtractor
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor
import com.github.scribejava.core.extractors.TokenExtractor
import com.github.scribejava.core.model.OAuth2AccessToken

class SpotifyApi20 protected constructor() : DefaultApi20() {
    private object InstanceHolder {
        internal val INSTANCE = com.nat.nat.apis.SpotifyApi20()
    }

    override fun getAccessTokenEndpoint(): String {
        return "https://accounts.spotify.com/api/token"
    }

    override fun getAuthorizationBaseUrl(): String {
        return "https://accounts.spotify.com/authorize"
    }

    override fun getAccessTokenExtractor(): TokenExtractor<OAuth2AccessToken> {
        return OAuth2AccessTokenJsonExtractor.instance()
    }


    companion object {
        fun instance(): SpotifyApi20 {
            return SpotifyApi20.InstanceHolder.INSTANCE
        }
    }
}