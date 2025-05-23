package com.example.gamedealshunter.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.gamedealshunter.data.prefs.SettingsRepository
import com.example.gamedealshunter.di.enqueuePriceChecker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SettingsViewModel(private val prefs: SettingsRepository,
                        private val wm: WorkManager) : ViewModel() {

    val minDiscount = prefs.minDiscount.stateIn(viewModelScope, SharingStarted.Eagerly, 30)
    val interval    = prefs.intervalHours.stateIn(viewModelScope, SharingStarted.Eagerly, 6)

    fun save(min: Int, hrs: Int) = viewModelScope.launch {
        prefs.save(min, hrs)
        enqueuePriceChecker(wm, hrs)
    }
}