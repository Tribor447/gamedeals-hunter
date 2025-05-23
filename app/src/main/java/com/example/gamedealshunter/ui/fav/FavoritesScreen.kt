package com.example.gamedealshunter.ui.fav

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel


import com.example.gamedealshunter.data.local.FavoriteEntity
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.ui.deals.components.DealCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    vm: FavoritesViewModel = koinViewModel()
) {
    val list by vm.favorites.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное") },
                navigationIcon = {
                    IconButton({ navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { inner ->
        if (list.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentAlignment = Alignment.Center
            ) { Text("Пусто") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) { fav ->


                    DealCard(
                        deal = fav.toDto(),
                        isFavorite = true,
                        onFavClick = { vm.remove(fav.dealId) }
                    )
                }
            }
        }
    }
}


private fun FavoriteEntity.toDto() = DealDto(
    id = dealId,
    title = title,
    thumb = thumb,
    salePrice = currentPrice,
    normalPrice = normalPrice,
    storeId = storeId
)
