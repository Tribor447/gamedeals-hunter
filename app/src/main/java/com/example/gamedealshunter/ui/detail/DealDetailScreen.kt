package com.example.gamedealshunter.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gamedealshunter.util.StoreNames
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

private val priceFmt = DecimalFormat("#0.00")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealDetailScreen(
    navController: NavController,
    vm: DealDetailViewModel = koinViewModel()
) {
    val detail      by vm.detail.collectAsState()
    val storeDeals  by vm.storeDeals.collectAsState()
    val favorites   by vm.favorites.collectAsState()

    if (detail == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val info       = detail!!.gameInfo
    val hiResThumb = remember(info.thumb) {
        info.thumb.replace("capsule_sm_120", "capsule_616x353")
    }
    val discount   = ((1 - info.salePrice.toDouble() / info.retailPrice.toDouble()) * 100).roundToInt()
    val isFavorite = favorites.any { it.dealId == vm.detail.value?.gameInfo?.steamAppId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(info.name) },
                navigationIcon = {
                    IconButton({ navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = vm::toggleFav) {
                        val icon = if (isFavorite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder
                        Icon(icon, null)
                    }
                }
            )
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(hiResThumb)
                    .crossfade(true)
                    .build(),
                contentDescription = info.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )

            Text("Текущая цена: $${priceFmt.format(info.salePrice.toDouble())}",
                style = MaterialTheme.typography.titleLarge)

            Text("Обычная цена: $${priceFmt.format(info.retailPrice.toDouble())}",
                style = MaterialTheme.typography.bodyMedium)

            Text("Скидка: -$discount%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary)

            Spacer(Modifier.height(8.dp))
            Text("Цены во всех магазинах", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(storeDeals) { deal ->
                    val storeName = StoreNames[deal.storeId]
                    val disc = ((1 - deal.salePrice / deal.normalPrice) * 100).roundToInt()

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text("$storeName  (-$disc%)")
                            Text("$${priceFmt.format(deal.salePrice)}")
                        }
                    }
                }
            }
        }
    }
}

