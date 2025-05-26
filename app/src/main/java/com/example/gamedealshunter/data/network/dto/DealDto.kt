package com.example.gamedealshunter.data.network.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class DealDto(
    @SerialName("dealID") val id: String,
    @SerialName("storeID") val storeId: Int,
    val title: String,
    @SerialName("salePrice") val salePrice: Double,
    @SerialName("normalPrice") val normalPrice: Double,
    val thumb: String
)
