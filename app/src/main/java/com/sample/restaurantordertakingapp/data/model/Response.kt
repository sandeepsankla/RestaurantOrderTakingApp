package com.sample.restaurantordertakingapp.data.model


data class OrderResponse(
    val orderId: String,
    val status: String
)

data class OrderStatus(
    val orderId: String,
    val status: String
)
