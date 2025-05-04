package com.example.gamedealshunter.ui.deals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.ui.deals.components.DealCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun DealsScreen(viewModel: DealsViewModel = koinViewModel()) {
    val pagingItems = viewModel.deals.collectAsLazyPagingItems()
    val snackHost = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        DealsList(pagingItems)
        SnackbarHost(snackHost, Modifier.align(Alignment.BottomCenter))
    }

    // простая обработка ошибок
    LaunchedEffect(pagingItems.loadState) {
        val error = pagingItems.loadState.refresh as? LoadState.Error
        error?.let { snackHost.showSnackbar("Ошибка загрузки: ${it.error.message}") }
    }
}

@Composable
fun DealsList(pagingItems: LazyPagingItems<DealDto>) {

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // ── основной контент ────────────────────────────────────
        items(count = pagingItems.itemCount) { index ->
            pagingItems[index]?.let { deal ->
                DealCard(deal)
            }
        }

        // ── footer «подгружаю…» ────────────────────────────────
        if (pagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
