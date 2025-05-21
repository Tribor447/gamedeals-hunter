package com.example.gamedealshunter.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.data.repository.DealsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.paging.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import androidx.paging.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update




class DealsViewModel(
    private val repo: DealsRepository
) : ViewModel() {

    private val _showPriceFilter = MutableStateFlow(false)
    val    showPriceFilter : StateFlow<Boolean> = _showPriceFilter
    fun togglePriceFilter() = _showPriceFilter.update { !it }

    private val _sort = MutableStateFlow(SortOrder.ASC)
    val    sort : StateFlow<SortOrder> = _sort
    fun onSortChange(o: SortOrder) { _sort.value = o }


    private val _query = MutableStateFlow("")
    val    query : StateFlow<String> = _query
    fun onQueryChange(q: String) { _query.value = q }


    private val _range = MutableStateFlow(0f..100f)
    val    range : StateFlow<ClosedFloatingPointRange<Float>> = _range
    fun onRangeChange(r: ClosedFloatingPointRange<Float>) { _range.value = r }


    private val basePager =
        repo.getDealsPager().cachedIn(viewModelScope)


    val deals: Flow<PagingData<DealDto>> =
        combine(_query, _range, _sort) { q, priceRange, sort ->
            Triple(q.lowercase(), priceRange, sort)
        }.flatMapLatest { (q, priceRange, sort) ->
            basePager
                .map { pd ->

                    val filtered = pd.filter { dto ->
                        dto.title.lowercase().contains(q) &&
                                dto.salePrice.toFloat() in priceRange
                    }

                    when (sort) {
                        SortOrder.ASC  -> filtered
                        SortOrder.DESC -> filtered.map { it }
                            .also {  }
                    }
                }
        }
    val favorites = repo.observeFavorites()
        .stateIn(viewModelScope,
            started     = SharingStarted.Eagerly,
            initialValue = emptyList())


    fun toggleFav(deal: DealDto) = viewModelScope.launch {
        val exists = favorites.value.any { it.dealId == deal.id }
        if (exists) repo.removeFav(id = deal.id)
        else repo.addToFav(dto = deal)
    }
}
