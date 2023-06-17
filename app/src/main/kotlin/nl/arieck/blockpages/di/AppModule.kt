package nl.arieck.blockpages.di

import nl.arieck.blockpages.data.di.dataModules
import nl.arieck.blockpages.ui.di.presentationModules
import org.koin.core.module.Module

/**
 * App Components
 */
// Gather all app modules
val appModules: List<Module> = presentationModules + dataModules
