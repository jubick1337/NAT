package com.nat.nat.services

import com.nat.nat.entity.ApiService
import com.nat.nat.entity.Playlist
import com.nat.nat.entity.Song
import khttp.get
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Service

@Service("youtubeApiWorkerService")
class YoutubeApiWorker : ApiWorker {
     override fun getPlaylist(oauthToken: String?): Playlist {
        val response = get(
            url = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&hl=RU_ru&maxResults=50&myRating=like&key=AIzaSyCKfe8gN2UQaeAzGkHijnp4A_5P0Z50mNE",
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
            val info = item.get("snippet") as JSONObject
            val author = info.get("channelTitle") as String
            if (" - Topic" in author) {
                songs.add(
                    Song(
                        name = info.get("title") as String,
                        author = (info.get("channelTitle") as String).removeSuffix(" - Topic")
                    )
                )
            }
        }
        return Playlist(songs = songs)
    }

    override fun addToFavorite(oauthToken: String?, playlist: Playlist) {
        TODO("Not yet implemented")
    }
}