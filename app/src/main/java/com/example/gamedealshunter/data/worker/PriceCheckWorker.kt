package com.example.gamedealshunter.data.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gamedealshunter.R
import com.example.gamedealshunter.data.network.CheapSharkApi
import com.example.gamedealshunter.data.prefs.SettingsRepository
import com.example.gamedealshunter.data.local.FavoritesDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.coroutines.flow.first


class PriceCheckWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params), KoinComponent {

    private val api: CheapSharkApi by inject()
    private val dao: FavoritesDao by inject()
    private val prefs: SettingsRepository by inject()

    override suspend fun doWork(): Result {
        val minDiscount = prefs.minDiscount.first()
        val favorites = dao.all().first()

        favorites.forEach { fav ->
            val deal = api.getDeals(pageSize = 1, storeId = null)
                .firstOrNull { it.id == fav.dealId } ?: return@forEach

            val discount = 100 * (1 - deal.salePrice / deal.normalPrice)
            if (discount >= minDiscount && deal.salePrice < fav.currentPrice) {
                notifyPriceDrop(fav.title, deal.salePrice)
                dao.add(fav.copy(currentPrice = deal.salePrice))
            }
        }
        return Result.success()
    }


    private fun notifyPriceDrop(title: String, newPrice: Double) {
        if (!NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()) {
            return
        }
        createChannelIfNeeded()
        NotificationManagerCompat.from(applicationContext)
            .notify(
                title.hashCode(),
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_local_offer_24)
                    .setContentTitle("Цена упала!")
                    .setContentText("$title теперь $newPrice $")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
            )
    }

    private fun createChannelIfNeeded() {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return

        val mgr = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as android.app.NotificationManager

        if (mgr.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                "Price drops",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            mgr.createNotificationChannel(channel)
        }
    }
    companion object { const val CHANNEL_ID = "price_drop" }
}
