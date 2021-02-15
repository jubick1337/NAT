package com.nat.nat.services

import com.nat.nat.entity.Playlist
import com.nat.nat.entity.Song
import khttp.get
import khttp.put
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

    private fun searchIds(oauthToken: String?,playlist: Playlist) : Collection<String>
    {

        val parseId: (JSONObject) -> String = { jsonObject: JSONObject ->
            val tracks = jsonObject.get("tracks") as JSONObject
            val items = tracks.get("items") as JSONArray
            val song = items.get(0) as JSONObject
            song.get("id") as String
        }

        val getId: (String,String) -> String = { name: String, author: String ->
            val response = get(
                url = "https://api.spotify.com/v1/search?q=${"$author $name".replace(" ", "%20", true)}&type=track",
                headers = mapOf(
                    "Accept" to "application/json",
                    "Content-Type" to "application/json",
                    "Authorization" to "Bearer ${oauthToken}"
                )
            )
            parseId(response.jsonObject)
        }

        var result: ArrayList<String> = ArrayList()

        playlist.songs.forEach { song ->
            result.add(getId(song.name,song.author))
        }

        return result
    }


    override fun addToFavorite(oauthToken: String?, playlist: Playlist) {
        val ids = searchIds(oauthToken,playlist)
        val chunkedIds: ArrayList<ArrayList<String>> = ArrayList()

        var chunk: ArrayList<String> = ArrayList()
        ids.forEach{id ->
            if(chunk.size < 50)
            {
                chunk.add(id)
            }
            else
            {
                chunkedIds.add(chunk)
                chunk = ArrayList()
            }
        }
        chunkedIds.add(chunk)

        val sendChunk =  { innerChunk: ArrayList<String> ->
            put(
                url = "https://api.spotify.com/v1/me/tracks?ids=${innerChunk.joinToString(",")}",
                headers = mapOf(
                    "Accept" to "application/json",
                    "Content-Type" to "application/json",
                    "Authorization" to "Bearer ${oauthToken}"
                )
            )
        }

        chunkedIds.forEach { innerChunk ->
            sendChunk(innerChunk)
        }

    }
}