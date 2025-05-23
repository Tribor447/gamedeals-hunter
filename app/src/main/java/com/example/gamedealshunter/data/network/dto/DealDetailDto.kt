package com.example.gamedealshunter.data.network.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class DealDetailDto(
    @SerialName("gameInfo") val gameInfo: GameInfoDto,
    @SerialName("cheaperStores") val cheaperStores: List<StoreDealDto> = emptyList()
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GameInfoDto(
    @SerialName("storeID")     val storeId: Int,
    @SerialName("name")        val name: String,
    @SerialName("salePrice")   val salePrice: String,
    @SerialName("retailPrice") val retailPrice: String,
    @SerialName("steamAppID")  val steamAppId: String? = null,
    val thumb: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StoreDealDto(
    @SerialName("storeID")     val storeId: Int,
    @SerialName("salePrice")   val salePrice: String,
    @SerialName("retailPrice") val retailPrice: String
)

