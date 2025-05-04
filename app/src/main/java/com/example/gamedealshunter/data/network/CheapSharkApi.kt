package com.example.gamedealshunter.data.network

import com.example.gamedealshunter.data.network.dto.DealDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {
    @GET("deals")
    suspend fun getDeals(
        @Query("pageNumber") page: Int = 0,
        @Query("pageSize") pageSize: Int = 60,
        @Query("storeID") storeId: Int? = null
    ): List<DealDto>
}
