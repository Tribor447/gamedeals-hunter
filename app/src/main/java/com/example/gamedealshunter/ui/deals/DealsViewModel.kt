package com.example.gamedealshunter.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gamedealshunter.data.local.FavoriteEntity
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.data.repository.DealsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DealsViewModel(
    private val repo: DealsRepository
) : ViewModel() {

    val deals = repo.getDealsPager().cachedIn(viewModelScope)
    val favorites = repo.observeFavorites().stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    fun toggleFav(deal: DealDto) = viewModelScope.launch {
        val exists = favorites.value.any { it.dealId == deal.id }
        if (exists) repo.removeFav(deal.id) else repo.addToFav(deal)
    }
}
