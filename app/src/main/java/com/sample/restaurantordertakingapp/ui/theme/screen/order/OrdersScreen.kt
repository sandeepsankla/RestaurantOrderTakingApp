package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrdersScreen() {
    val dummyOrders = listOf(
        OrderUi(
            id = "101",
            items = 3,
            amount = 450,
            status = "Completed"
        ),
        OrderUi(
            id = "102",
            items = 2,
            amount = 280,
            status = "Placed"
        ),
        OrderUi(
            id = "103",
            items = 5,
            amount = 820,
            status = "Completed"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Orders",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        // TEMP: Dummy orders list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items( count =dummyOrders.size ) { it ->
                OrderItem(order = dummyOrders[it])
            }
        }
    }
}

