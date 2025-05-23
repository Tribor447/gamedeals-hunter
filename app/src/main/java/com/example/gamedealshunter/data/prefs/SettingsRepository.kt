package com.example.gamedealshunter.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsRepository(private val context: Context) {

    companion object {
        private val KEY_MIN_DISCOUNT = intPreferencesKey("min_discount")
        private val KEY_INTERVAL_HRS = intPreferencesKey("interval_hours")
    }

    val minDiscount: Flow<Int> = context.dataStore.data.map { it[KEY_MIN_DISCOUNT] ?: 30 }
    val intervalHours: Flow<Int> = context.dataStore.data.map { it[KEY_INTERVAL_HRS] ?: 6 }

    suspend fun save(minDiscount: Int, interval: Int) = context.dataStore.edit {
        it[KEY_MIN_DISCOUNT] = minDiscount
        it[KEY_INTERVAL_HRS] = interval
    }
}
