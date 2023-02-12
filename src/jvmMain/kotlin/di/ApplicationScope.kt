package di

import domain.PlaylistGenerator
import network.*
import view.ViewModel

object ApplicationScope {

    val tokenHolder: AccessTokenHolder by lazy {
        AccessTokenHolder()
    }

    val authRepository by lazy {
        AuthRepository(provideNoAuthClient(), tokenHolder)
    }

    val playlistsNetworkDataSource by lazy {
        PlaylistsNetworkDataSource(provideAuthClient())
    }

    val generator by lazy {
        PlaylistGenerator(playlistsNetworkDataSource, authRepository)
    }

    val viewModel by lazy {
        ViewModel(generator)
    }
}