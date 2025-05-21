package com.example.gamedealshunter.ui.detail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.data.repository.DealsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DealDetailViewModel(
    state: SavedStateHandle,
    private val repo: DealsRepository
) : ViewModel() {

    private val dealId: String = Uri.decode(state["dealId"] ?: "")

    val detail: StateFlow<com.example.gamedealshunter.data.network.dto.DealDetailDto?> =
        flow { emit(repo.getDealDetail(dealId)) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val storeDeals: StateFlow<List<DealDto>> =
        detail.flatMapLatest { det ->
            val steam = det?.gameInfo?.steamAppId?.toIntOrNull()
            if (steam == null) flowOf(emptyList())
            else flow { emit(repo.getDealsForGame(steam)) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val favorites = repo.observeFavorites()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggleFav() = viewModelScope.launch {
        val info = detail.value?.gameInfo ?: return@launch
        val favExists = favorites.value.any { it.dealId == dealId }

        val dto = DealDto(
            id          = dealId,
            storeId     = info.storeId,
            title       = info.name,
            salePrice   = info.salePrice.toDouble(),
            normalPrice = info.retailPrice.toDouble(),
            thumb       = info.thumb
        )

        if (favExists) repo.removeFav(dealId) else repo.addToFav(dto)
    }
}

