package com.example.gamedealshunter.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.data.repository.DealsRepository
import kotlinx.coroutines.flow.Flow

class DealsViewModel(
    repository: DealsRepository
) : ViewModel() {
    val deals: Flow<PagingData<DealDto>> =
        repository.getDealsPager().cachedIn(viewModelScope)
}
