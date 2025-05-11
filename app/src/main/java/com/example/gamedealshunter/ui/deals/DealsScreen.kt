package com.example.gamedealshunter.ui.deals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gamedealshunter.data.local.FavoriteEntity
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.ui.deals.components.DealCard
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen(
    navController: NavController,
    viewModel: DealsViewModel = koinViewModel()
) {



    val pagingItems: LazyPagingItems<DealDto> =
        viewModel.deals.collectAsLazyPagingItems()


    val favIds by viewModel.favorites
        .map { list -> list.map(FavoriteEntity::dealId).toSet() }
        .collectAsState(initial = emptySet())


    val snackHost = remember { SnackbarHostState() }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Deals") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackHost) }
    ) { innerPadding: PaddingValues ->


        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            items(count = pagingItems.itemCount) { index ->
                pagingItems[index]?.let { deal ->
                    DealCard(
                        deal        = deal,
                        isFavorite  = favIds.contains(deal.id),
                        onFavClick  = viewModel::toggleFav
                    )
                }
            }


            item {
                if (pagingItems.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
            }
        }


        val refreshError = pagingItems.loadState.refresh as? LoadState.Error
        LaunchedEffect(refreshError) {
            refreshError?.let {
                snackHost.showSnackbar(
                    it.error.localizedMessage ?: "Network error"
                )
            }
        }
    }
}


