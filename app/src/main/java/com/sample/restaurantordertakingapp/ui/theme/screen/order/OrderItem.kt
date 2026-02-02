package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.domain.model.OrderStatus

@Composable
fun OrderItem(order: OrderUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = "Order #${order.orderId}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Items: ${order.itemsText}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Total: ₹${order.totalAmount}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = order.status.name,
                color = if (order.status.equals(OrderStatus.DELIVERED.name))
                    Color(0xFF2E7D32)
                else
                    Color(0xFFF57C00),
                style = MaterialTheme.typography.bodySmall

            )
        }
    }
}
