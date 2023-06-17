package nl.arieck.blockpages.data.di

import nl.arieck.blockpages.data.features.character.CharacterRepositoryImpl
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import org.koin.dsl.module


/**
 * Data Components
 */
private val repositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
}

// Gather all app modules
val dataModules =
    listOf(
        networkModule,
        repositoryModule,
    )
