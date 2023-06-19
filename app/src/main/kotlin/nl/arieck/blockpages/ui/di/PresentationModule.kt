package nl.arieck.blockpages.ui.di

import nl.arieck.blockpages.ui.features.character.CharacterViewModel
import nl.arieck.blockpages.ui.features.character.detail.CharacterDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val presentationModule = module {

}

private val viewModelModule = module {
    viewModel { CharacterViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}

val presentationModules = listOf(presentationModule, viewModelModule)