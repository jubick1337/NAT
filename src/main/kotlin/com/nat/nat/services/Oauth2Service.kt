package com.nat.nat.services

import com.github.scribejava.core.model.OAuth2AccessToken
import org.springframework.stereotype.Service


interface Oauth2Service {
    fun getUrl(): String?
    fun getToken(code: String) : OAuth2AccessToken?
}