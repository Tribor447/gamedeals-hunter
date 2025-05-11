package com.example.gamedealshunter.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gamedealshunter.data.network.CheapSharkApi
import com.example.gamedealshunter.data.network.dto.DealDto
import kotlinx.coroutines.flow.Flow

class DealsRepository(
    private val api: CheapSharkApi
) {
    fun getDealsPager(storeId: Int? = null): Flow<PagingData<DealDto>> =
        Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = false),
            pagingSourceFactory = { DealsPagingSource(api, storeId) }
        ).flow
}
