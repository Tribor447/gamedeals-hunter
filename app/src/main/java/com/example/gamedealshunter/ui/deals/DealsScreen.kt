package com.example.gamedealshunter.ui.deals

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gamedealshunter.ui.deals.components.DealCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen(
    navController: NavController,
    viewModel: DealsViewModel = koinViewModel()
) {
    val showPrice by viewModel.showPriceFilter.collectAsState()
    val sortOrder by viewModel.sort.collectAsState()
    var sortMenu by remember { mutableStateOf(false) }
    val range by viewModel.range.collectAsState()
    val favIds by viewModel.favoriteIds.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchField(
                        text = viewModel.query.collectAsState().value,
                        onTextChange = viewModel::onQueryChange
                    )
                },
                actions = {
                    IconButton({ sortMenu = true }) {
                        Icon(Icons.Default.Tune, contentDescription = "Сортировка/фильтр")
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
                        Icon(Icons.Default.Favorite, contentDescription = "Избранное")
                    }
                    IconButton({ navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { innerPadding ->


        val pagingItems = viewModel.deals.collectAsLazyPagingItems()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding()
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
                val sorted = if (sortOrder == SortOrder.ASC)
                    snapshot.sortedBy { it.salePrice }
                else
                    snapshot.sortedByDescending { it.salePrice }

                itemsIndexed(sorted, key = { _, d -> d.id }) { _, deal ->
                    DealCard(
                        deal = deal,
                        isFavorite = favIds.contains(deal.id),
                        onFavClick = viewModel::toggleFav,
                        onClick = {
                            navController.navigate("deal/${Uri.encode(deal.id)}")
                        }
                    )
                }
            }
        }
    }
}



@Composable
private fun SearchField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        placeholder = { Text("Поиск…") },
        textStyle = MaterialTheme.typography.bodyLarge,
        leadingIcon = { Icon(Icons.Default.Search, null) },
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
                colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}



