package network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import model.PlaylistId
import model.PlaylistPaging
import model.PlaylistTrackResponse
import model.Track

class PlaylistsNetworkDataSource(private val client: HttpClient) {

    companion object {
        private val GET_TOPLISTS_PATH = "browse/categories/toplists/playlists"
        private val GET_PLAYLIST_TRACKS_PATH_FORMAT = "playlists/%s/tracks"
    }

    suspend fun getTopPlaylists(): List<PlaylistId> {
        return client.get(GET_TOPLISTS_PATH).body<PlaylistPaging>().playlists.items
    }

    suspend fun getPlaylistTracks(id: String): List<Track> {
        return client.get(GET_PLAYLIST_TRACKS_PATH_FORMAT.format(id)).body<PlaylistTrackResponse>().items.map { it.track }
    }
}