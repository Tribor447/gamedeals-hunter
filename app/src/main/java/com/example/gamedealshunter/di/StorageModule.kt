package com.example.gamedealshunter.di
import androidx.room.Room
import com.example.gamedealshunter.data.local.AppDatabase
import com.example.gamedealshunter.data.prefs.SettingsRepository
import org.koin.dsl.module

val storageModule = module {


    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "game_deals.db").fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().favoritesDao() }


    single { SettingsRepository(get()) }
}
