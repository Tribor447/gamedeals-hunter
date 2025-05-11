package com.example.gamedealshunter.ui.deals.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

import kotlin.math.roundToInt
import java.text.DecimalFormat

private val priceFormat = DecimalFormat("#0.00")

@Composable
fun DealCard(
    deal: DealDto,
    isFavorite: Boolean,
    onFavClick: (DealDto) -> Unit,
    modifier: Modifier = Modifier
) {
    val discount = remember(deal) {
        ((1 - deal.salePrice / deal.normalPrice) * 100).roundToInt()
    }
    val store = remember(deal.storeId) { StoreNames[deal.storeId] }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(12.dp)) {


            AsyncImage(
                model = deal.thumb,
                contentDescription = deal.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(12.dp))


            Column(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(deal.title, style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(4.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$${priceFormat.format(deal.salePrice)}",
                        style = MaterialTheme.typography.titleLarge)

                    Spacer(Modifier.width(8.dp))

                    Text("$${priceFormat.format(deal.normalPrice)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f))
                }

                Spacer(Modifier.height(4.dp))


                Text(
                    text = "$store · -$discount%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }


            IconButton(onClick = { onFavClick(deal) }) {
                val icon = if (isFavorite) Icons.Filled.Favorite
                else Icons.Outlined.FavoriteBorder
                Icon(icon, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}