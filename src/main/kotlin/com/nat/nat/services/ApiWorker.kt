package com.nat.nat.services

import com.nat.nat.entity.Playlist
import com.nat.nat.entity.ApiService

interface ApiWorker {
    fun getPlaylist(oauthToken: String?): Playlist
    fun addToFavorite(oauthToken: String?, playlist: Playlist)
}