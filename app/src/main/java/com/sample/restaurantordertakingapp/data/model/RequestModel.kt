package com.sample.restaurantordertakingapp.data.model

data class OrderRequest(
    val tableId: String,
    val items: List<OrderItem>
)
