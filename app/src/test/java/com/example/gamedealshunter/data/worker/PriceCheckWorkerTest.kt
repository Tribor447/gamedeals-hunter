package com.example.gamedealshunter.data.worker

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.ListenableWorker.Result
import androidx.test.core.app.ApplicationProvider
import com.example.gamedealshunter.data.local.AppDatabase
import com.example.gamedealshunter.data.local.FavoriteEntity
import com.example.gamedealshunter.data.local.FavoritesDao
import com.example.gamedealshunter.data.network.CheapSharkApi
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.data.prefs.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNotificationManager
import androidx.room.Room
import com.example.gamedealshunter.data.network.dto.DealDetailDto
import org.robolectric.Shadows.shadowOf

import org.junit.runner.RunWith
@Config(manifest = Config.NONE, sdk = [28])
@RunWith(RobolectricTestRunner::class)
class PriceCheckWorkerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: AppDatabase
    private lateinit var dao: FavoritesDao

    @Before
    fun setUp(){

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.favoritesDao()
        runBlocking {
            dao.add(
                FavoriteEntity(
                    dealId = "deal1",
                    title = "Test Game",
                    thumb = "",
                    currentPrice = 100.0,
                    normalPrice = 100.0,
                    storeId = 2
                )
            )
        }

        val fakeApi = object : CheapSharkApi {
            override suspend fun getDeals(
                page: Int,
                pageSize: Int,
                storeId: Int?,
                title: String?
            ): List<DealDto> = listOf(
                DealDto("deal1", 2, "Test Game", 50.0, 100.0, "")
            )
            override suspend fun getDealDetail(id: String): DealDetailDto =
                error("not needed")

            override suspend fun getDealsBySteamApp(steamAppId: Int, pageSize: Int): List<DealDto> =
                emptyList()


        }

        startKoin {
            modules(
                module {
                    single<CheapSharkApi> { fakeApi }
                    single<FavoritesDao> { dao }
                    single<SettingsRepository> { SettingsRepository(context) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun worker_posts_notification_when_price_drops_enough() = runBlocking {

        val worker = TestListenableWorkerBuilder<PriceCheckWorker>(context)
            .build()

        val result = worker.doWork()
        assertEquals(Result.success(), result)


        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val shadowNm = shadowOf(nm) as ShadowNotificationManager
        val notifications = shadowNm.allNotifications
        assertEquals(1, notifications.size)

        val notif: Notification = notifications[0]!!
        assertEquals("Цена упала!", notif.extras.getString(Notification.EXTRA_TITLE))
        val text = notif.extras.getString(Notification.EXTRA_TEXT) ?: ""
        assertTrue(text.contains("Test Game"))
        assertTrue(text.contains("50.0"))
    }
}
