package com.example.gamedealshunter.ui.deals.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gamedealshunter.data.network.dto.DealDto
import com.example.gamedealshunter.util.StoreNames
import androidx.compose.foundation.clickable
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

import kotlin.math.roundToInt
import java.text.DecimalFormat

private val priceFormat = DecimalFormat("#0.00")

@Composable
fun DealCard(
    modifier: Modifier = Modifier,
    deal: DealDto,
    isFavorite: Boolean,
    onFavClick: (DealDto) -> Unit,
    onClick: () -> Unit = {}

) {
    val discount = remember(deal) {
        ((1 - deal.salePrice / deal.normalPrice) * 100).roundToInt()
    }
    val store = remember(deal.storeId) { StoreNames[deal.storeId] }

    val ctx = LocalContext.current

    val imgUrl = remember(deal.thumb) {
        deal.thumb.replace("capsule_sm_120", "capsule_616x353")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {

            AsyncImage(
                model = ImageRequest.Builder(ctx)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = deal.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.small)
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(deal.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f))

                // Кнопка избранного
                IconButton(onClick = { onFavClick(deal) }) {
                    val icon =
                        if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                    Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$${priceFormat.format(deal.salePrice)}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    "$${priceFormat.format(deal.normalPrice)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
                )

                Spacer(Modifier.width(8.dp))

                AssistChip(
                    onClick = {},
                    label = {
                        Text("-$discount%")
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        labelColor     = MaterialTheme.colorScheme.onSecondary
                    ),
                    border = null
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(
                text = store,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}