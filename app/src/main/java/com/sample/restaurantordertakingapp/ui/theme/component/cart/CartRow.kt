package com.sample.restaurantordertakingapp.ui.theme.component.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.data.model.CartItem
import com.sample.restaurantordertakingapp.data.model.CartState
import com.sample.restaurantordertakingapp.ui.theme.component.common.QuantitySelector


@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    showInStock: Boolean = true,
    showRemoveButton: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Item Name
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // Unit Price
                Text(
                    text = "Unit: $${"%.2f".format(item)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // In Stock badge (optional)
                if (item!= null && showInStock) {
                    Text(
                        text = "In Stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Green,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Total Price for this item
                Text(
                    text = "$${"%.2f".format(item.price * item.quantity)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Remove Button (optional)
                if (showRemoveButton) {
                    TextButton(
                        onClick = onRemove,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Remove",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            }
        }

        // Quantity Selector
        QuantitySelector(
            quantity = item.quantity,
            onQuantityChange = onQuantityChange,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}