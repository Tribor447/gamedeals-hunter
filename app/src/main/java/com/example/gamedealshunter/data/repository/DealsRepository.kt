package com.example.gamedealshunter.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gamedealshunter.data.local.FavoriteEntity
import com.example.gamedealshunter.data.local.FavoritesDao
import com.example.gamedealshunter.data.network.CheapSharkApi
import com.example.gamedealshunter.data.network.dto.*
import kotlinx.coroutines.flow.Flow

class DealsRepository(
    private val api: CheapSharkApi,
    private val dao: FavoritesDao
) {
    fun getDealsPager(storeId: Int? = null): Flow<PagingData<DealDto>> =
        Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = false),
            pagingSourceFactory = { DealsPagingSource(api, storeId) }
        ).flow
    suspend fun addToFav(dto: DealDto) {
        val entity = dao.find(dto.id)
        if (entity == null) {
            dao.add(
                FavoriteEntity(
                    dealId       = dto.id,
                    title        = dto.title,
                    thumb        = dto.thumb,
                    currentPrice = dto.salePrice,
                    normalPrice  = dto.normalPrice,
                    storeId      = dto.storeId
                )
            )
        } else {
            dao.remove(dto.id)
        }
    }

    suspend fun removeFav(id: String) = dao.remove(id)

    fun observeFavorites(): Flow<List<FavoriteEntity>> = dao.all()

    suspend fun getDealDetail(id: String): DealDetailDto = api.getDealDetail(id)
    suspend fun getDealsForGame(steamAppId: Int): List<DealDto> =
        api.getDealsBySteamApp(steamAppId)
}
