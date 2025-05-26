package com.example.gamedealshunter

import android.app.Application
import com.example.gamedealshunter.di.repositoryModule
import com.example.gamedealshunter.di.viewModelModule
import com.example.gamedealshunter.data.network.networkModule
import com.example.gamedealshunter.di.storageModule
import com.example.gamedealshunter.di.workerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import androidx.work.WorkManager
import com.example.gamedealshunter.di.enqueuePriceChecker
import org.koin.core.component.get
import org.koin.core.component.KoinComponent
class GameDealsApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GameDealsApp)
            modules(networkModule, repositoryModule, viewModelModule, storageModule,
                workerModule)
        }
        enqueuePriceChecker(get<WorkManager>())
    }
}
