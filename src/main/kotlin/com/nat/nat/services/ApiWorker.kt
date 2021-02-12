package com.nat.nat.services

import com.nat.nat.entity.Playlist

interface ApiWorker {
    fun getPlaylist(oauthToken: String?): Playlist
    fun migrateTo()
}