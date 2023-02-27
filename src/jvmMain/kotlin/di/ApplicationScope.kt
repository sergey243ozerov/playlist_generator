package di

import domain.GeneratorRepository
import network.*
import view.ViewModel

object ApplicationScope {

    val repository by lazy {
        GeneratorRepository(provideNoAuthClient())
    }

    val viewModel by lazy {
        ViewModel(repository)
    }
}