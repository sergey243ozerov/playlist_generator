package di

import network.AccessTokenHolder
import network.AuthRepository
import network.PlaylistsNetworkDataSource
import network.provideAuthClient

object ApplicationScope {

    val tokenHolder: AccessTokenHolder by lazy {
        AccessTokenHolder()
    }

    val authRepository by lazy {
        AuthRepository(provideAuthClient(), tokenHolder)
    }

    val playlistsNetworkDataSource by lazy {
        PlaylistsNetworkDataSource(provideAuthClient())
    }
}