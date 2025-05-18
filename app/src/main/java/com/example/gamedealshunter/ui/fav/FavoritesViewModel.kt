package com.example.gamedealshunter.ui.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamedealshunter.data.local.FavoritesDao
import com.example.gamedealshunter.data.repository.DealsRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val dao: FavoritesDao,
    private val repo: DealsRepository
) : ViewModel() {


    val favorites = dao.all()
        .map { list -> list.sortedBy { it.title } }

    fun remove(dealId: String) = viewModelScope.launch {

        dao.remove(dealId)
    }

    fun toggle(deal: com.example.gamedealshunter.data.network.dto.DealDto) =
        viewModelScope.launch {
            repo.addToFav(deal)
        }
}
