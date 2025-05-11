package com.example.gamedealshunter

import android.app.Application
import com.example.gamedealshunter.di.repositoryModule
import com.example.gamedealshunter.di.viewModelModule
import com.example.gamedealshunter.data.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GameDealsApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GameDealsApp)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }
}
