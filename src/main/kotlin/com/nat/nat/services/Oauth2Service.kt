package com.nat.nat.services

import com.github.scribejava.core.model.OAuth2AccessToken


interface Oauth2Service {
    fun getUrl(): String?
    fun getToken(code: String) : OAuth2AccessToken?
}