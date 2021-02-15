package com.nat.nat.services

import com.nat.nat.entity.ApiService
import com.nat.nat.entity.Playlist
import com.nat.nat.entity.Song
import khttp.get
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service("youtubeApiWorkerService")
class YoutubeApiWorker(
    @Autowired
    final val env: Environment?
) : ApiWorker {

    private val apiKey: String? = this.env?.getProperty("google.api.key")
    override fun getPlaylist(oauthToken: String?): Playlist {
        val response = get(
            url = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&hl=RU_ru&maxResults=50&myRating=like&key=$apiKey",
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

    private fun searchIds(oauthToken: String?, playlist: Playlist): Collection<String> {

        val parseId: (JSONObject) -> String = { jsonObject: JSONObject ->
            val items = jsonObject.get("items") as JSONArray
            val item = items.get(0) as JSONObject
            val id = item.get("id") as JSONObject
            if (items.length() > 0) {
                id.get("videoId") as String
            } else {
                ""
            }
        }

        val getId: (String, String) -> String = { name: String, author: String ->
            val response = get(
                url = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&?q=${
                    "$author $name".replace(
                        " ",
                        "%20",
                        true
                    )
                }&videoCategoryId=10&type=video",
                headers = mapOf(
                    "Accept" to "application/json",
                    "Authorization" to "Bearer ${oauthToken}"
                )
            )
            parseId(response.jsonObject)
        }

        var result: ArrayList<String> = ArrayList()

        playlist.songs.forEach { song ->
            val id = getId(song.name, song.author)
            if (id != "") {
                result.add(id)
            }
        }

        return result
    }

    override fun addToFavorite(oauthToken: String?, playlist: Playlist) {

        val ids = searchIds(oauthToken, playlist)
        val sendId = { id: String ->
            val url =
                "https://youtube.googleapis.com/youtube/v3/videos/rate?id=$id&rating=like&key=$apiKey"
            val client = OkHttpClient()
            val json = MediaType.get("application/json")
            val body = RequestBody.create(json, "")
            val request = Request.Builder()
                .addHeader("Authorization", "Bearer $oauthToken")
                .url(url)
                .post(body)
                .build()
            client.newCall(request).execute()
        }
        ids.forEach { id -> sendId(id) }
    }
}