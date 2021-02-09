package com.nat.nat.apis

import com.fasterxml.jackson.databind.JsonNode
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor
import com.github.scribejava.core.model.OAuth2AccessToken

class SpotifyJsonTokenExtractor : OAuth2AccessTokenJsonExtractor() {
    override fun createToken(
        accessToken: String?,
        tokenType: String?,
        expiresIn: Int?,
        refreshToken: String?,
        scope: String?,
        response: JsonNode?,
        rawResponse: String?
    ): OAuth2AccessToken {
        return super.createToken(accessToken, tokenType, expiresIn, refreshToken, scope, response, rawResponse)
    }
}