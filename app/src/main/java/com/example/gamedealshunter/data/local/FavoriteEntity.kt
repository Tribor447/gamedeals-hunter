package com.example.gamedealshunter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val dealId: String,
    val title: String,
    val thumb: String,
    val currentPrice: Double,
    val normalPrice: Double,
    val storeId: Int
)