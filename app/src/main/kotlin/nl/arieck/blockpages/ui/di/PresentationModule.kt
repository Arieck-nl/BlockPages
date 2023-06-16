package nl.arieck.blockpages.ui.di

import nl.arieck.blockpages.ui.features.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val presentationModule = module {

}

private val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}

val presentationModules = listOf(presentationModule, viewModelModule)