package com.example.gamedealshunter.di

import com.example.gamedealshunter.data.repository.DealsRepository
import com.example.gamedealshunter.ui.deals.DealsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.gamedealshunter.data.local.FavoritesDao
import androidx.work.WorkManager
import com.example.gamedealshunter.ui.settings.SettingsViewModel
import com.example.gamedealshunter.ui.fav.FavoritesViewModel

val repositoryModule = module {
    single { DealsRepository(get(), get<FavoritesDao>()) }
}

val viewModelModule = module {
    viewModel { DealsViewModel(get()) }
    viewModel { SettingsViewModel(get(), get<WorkManager>()) }
    viewModel { FavoritesViewModel(get(), get()) }

}
