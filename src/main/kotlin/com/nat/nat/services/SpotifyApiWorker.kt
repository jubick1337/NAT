package com.nat.nat.services

import com.nat.nat.entity.Playlist
import com.nat.nat.entity.Song
import khttp.get
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Service

@Service("spotifyApiWorkerService")
class SpotifyApiWorker : ApiWorker {

    override fun getPlaylist(oauthToken: String?): Playlist {
        val response = get(
            url = "https://api.spotify.com/v1/me/tracks",
            headers = mapOf(
                "Accept" to "application/json",
                "Content-Type" to "application/json",
                "Authorization" to "Bearer ${oauthToken}"
            )
        )
        val jsonResponse: JSONObject = response.jsonObject
        val songs = ArrayList<Song>()
        val items = jsonResponse.get("items") as JSONArray
        (0..items.length() - 1).forEach {
            val item = items.get(it) as JSONObject
            val track = item.get("track") as JSONObject
            val trackName = track.get("name") as String
            val artists = track.get("artists") as JSONArray
            var artistName = ""
            (0..artists.length() - 1).forEach {
                artistName += (artists.get(it) as JSONObject).get("name")
                artistName += " "
            }
            artistName = artistName.trim()
            songs.add(Song(name = trackName, author = artistName))
        }
        return Playlist(songs)
    }

    override fun migrateTo() {
        TODO("Not yet implemented")
    }
}