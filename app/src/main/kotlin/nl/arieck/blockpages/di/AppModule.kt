package nl.arieck.blockpages.di

import nl.arieck.blockpages.ui.di.presentationModules
import org.koin.core.module.Module

/**
 * App Components
 */
// Gather all app modules
val appModules = listOf<Module>()
    .plus(presentationModules)
