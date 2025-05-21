package com.example.gamedealshunter.ui.deals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gamedealshunter.ui.deals.components.DealCard
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen(
    navController: NavController,
    viewModel: DealsViewModel = koinViewModel()
) {


    val showPrice by viewModel.showPriceFilter.collectAsState()
    val sortOrder by viewModel.sort.collectAsState()


    var sortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = viewModel.query.collectAsState().value,
                        onValueChange = viewModel::onQueryChange,
                        singleLine = true,
                        placeholder = { Text("Поиск…") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton({ sortMenu = true }) {
                        Icon(Icons.Default.Tune, "Сортировка/фильтр")
                    }
                    DropdownMenu(
                        expanded = sortMenu,
                        onDismissRequest = { sortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Цена ↑") },
                            onClick = {
                                viewModel.onSortChange(SortOrder.ASC)
                                sortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Цена ↓") },
                            onClick = {
                                viewModel.onSortChange(SortOrder.DESC)
                                sortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (showPrice) "Скрыть фильтр" else "Показать фильтр по цене") },
                            onClick = {
                                viewModel.togglePriceFilter()
                                sortMenu = false
                            }
                        )
                    }

                    IconButton({ navController.navigate("favorites") }) {
                        Icon(Icons.Default.Favorite, "Избранное")
                    }
                    IconButton({ navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, "Настройки")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { innerPadding: PaddingValues ->

        val range by viewModel.range.collectAsState()
        val pagingItems = viewModel.deals.collectAsLazyPagingItems()
        val favIds by viewModel.favorites
            .map { it.map { f -> f.dealId } }
            .collectAsState(initial = emptyList())
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            AnimatedVisibility(visible = showPrice) {
                Column {
                    RangeSlider(
                        value = range,
                        onValueChange = viewModel::onRangeChange,
                        valueRange = 0f..100f,
                        steps = 19,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "от $${range.start.toInt()}  до $${range.endInclusive.toInt()}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                val snapshot = pagingItems.itemSnapshotList.items
                val sorted = when (sortOrder) {
                    SortOrder.ASC -> snapshot.sortedBy { it.salePrice.toFloat() }
                    SortOrder.DESC -> snapshot.sortedByDescending { it.salePrice.toFloat() }
                }

                itemsIndexed(sorted, key = { _, d -> d.id }) { _, deal ->
                    DealCard(
                        deal = deal,
                        isFavorite = favIds.contains(deal.id),
                        onFavClick = viewModel::toggleFav
                    )
                }
            }
        }
    }
}


