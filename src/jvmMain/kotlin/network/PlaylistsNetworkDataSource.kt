package network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import model.PlaylistId

class PlaylistsNetworkDataSource(private val client: HttpClient) {

    companion object {
        private val GET_TOPLISTS_PATH = "browse/categories/toplists/playlists"
    }

    suspend fun getTopPlaylists(): List<PlaylistId> {
        return client.get(GET_TOPLISTS_PATH).body()
    }
}