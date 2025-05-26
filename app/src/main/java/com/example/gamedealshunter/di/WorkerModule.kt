package com.example.gamedealshunter.di
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gamedealshunter.data.worker.PriceCheckWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val workerModule = module {
    worker { PriceCheckWorker(get(), get()) }

    single { WorkManager.getInstance(get()) }
}

fun enqueuePriceChecker(workManager: WorkManager, intervalHours: Int = 6) {
    val request = PeriodicWorkRequestBuilder<PriceCheckWorker>(
        intervalHours.toLong(), TimeUnit.HOURS
    ).build()
    workManager.enqueueUniquePeriodicWork(
        "price-check", ExistingPeriodicWorkPolicy.UPDATE, request
    )
}
