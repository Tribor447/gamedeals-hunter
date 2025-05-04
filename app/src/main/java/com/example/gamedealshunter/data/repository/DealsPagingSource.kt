package com.example.gamedealshunter.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gamedealshunter.data.network.CheapSharkApi
import com.example.gamedealshunter.data.network.dto.DealDto

class DealsPagingSource(
    private val api: CheapSharkApi,
    private val storeId: Int? = null
) : PagingSource<Int, DealDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DealDto> {
        val page = params.key ?: 0
        return try {
            val deals = api.getDeals(page = page, pageSize = params.loadSize, storeId = storeId)
            LoadResult.Page(
                data = deals,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (deals.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DealDto>): Int? =
        state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}
