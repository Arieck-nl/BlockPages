package nl.arieck.blockpages.ui

import android.app.Application
import nl.arieck.blockpages.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class BlockPagesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    protected open fun initKoin() {
        // start Koin context
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BlockPagesApplication)
            modules(appModules)
        }
    }
}