package com.example.gamedealshunter.di

import com.example.gamedealshunter.data.repository.DealsRepository
import com.example.gamedealshunter.ui.deals.DealsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { DealsRepository(get()) }
}

val viewModelModule = module {
    viewModel { DealsViewModel(get()) }
}
