package com.example.gamedealshunter.data.network

import com.example.gamedealshunter.data.network.dto.*
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {
    @GET("deals")
    suspend fun getDeals(
        @Query("pageNumber") page: Int = 0,
        @Query("pageSize") pageSize: Int = 60,
        @Query("storeID") storeId: Int? = null,
        @Query("title")   title: String? = null
    ): List<DealDto>

    @GET("deals")
    suspend fun getDealDetail(
        @Query("id") dealId: String
    ): DealDetailDto

    @GET("deals")
    suspend fun getDealsBySteamApp(
        @Query("steamAppID") steamAppId: Int,
        @Query("pageSize") pageSize: Int = 60
    ): List<DealDto>
}
