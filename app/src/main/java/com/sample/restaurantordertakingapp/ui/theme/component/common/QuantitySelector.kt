package com.sample.restaurantordertakingapp.ui.theme.component.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*


enum class QuantitySelectorSize {
    SMALL, MEDIUM, LARGE
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minQuantity: Int = 1,
    maxQuantity: Int = 99,
    showLabel: Boolean = false,
    enabled: Boolean = true,
    size: QuantitySelectorSize = QuantitySelectorSize.MEDIUM
) {
    val configuration = remember(size) {
        when (size) {
            QuantitySelectorSize.SMALL -> QuantityConfig(
                iconSize = 20.dp,
                padding = 4.dp,
                height = 32.dp,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                shape = RoundedCornerShape(6.dp)
            )
            QuantitySelectorSize.MEDIUM -> QuantityConfig(
                iconSize = 24.dp,
                padding = 6.dp,
                height = 40.dp,
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                shape = RoundedCornerShape(8.dp)
            )
            QuantitySelectorSize.LARGE -> QuantityConfig(
                iconSize = 28.dp,
                padding = 8.dp,
                height = 48.dp,
                textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }

    Column(modifier = modifier) {
        if (showLabel) {
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(configuration.height)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    configuration.shape
                )
        ) {
            // Decrease button
            IconButton(
                onClick = {
                    if (quantity > minQuantity) onQuantityChange(quantity - 1)
                },
                modifier = Modifier
                    .size(configuration.iconSize + configuration.padding * 2)
                    .padding(configuration.padding),
                enabled = enabled && quantity > minQuantity
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = "Decrease quantity",
                    tint = if (enabled && quantity > minQuantity) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier.size(configuration.iconSize)
                )
            }

            // Quantity display
            Text(
                text = quantity.toString(),
                style = configuration.textStyle,
                fontWeight = FontWeight.Medium,
                color = if (enabled) MaterialTheme.colorScheme.onBackground else Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Increase button
            IconButton(
                onClick = {
                    if (quantity < maxQuantity) onQuantityChange(quantity + 1)
                },
                modifier = Modifier
                    .size(configuration.iconSize + configuration.padding * 2)
                    .padding(configuration.padding),
                enabled = enabled && quantity < maxQuantity
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase quantity",
                    tint = if (enabled && quantity < maxQuantity) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier.size(configuration.iconSize)
                )
            }
        }
    }
}

// Configuration data class
data class QuantityConfig(
    val iconSize: Dp,
    val padding: Dp,
    val height: Dp,
    val textStyle: androidx.compose.ui.text.TextStyle,
    val shape: Shape
)






