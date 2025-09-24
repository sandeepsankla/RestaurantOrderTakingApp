package com.sample.restaurantordertakingapp.data.model

data class OrderItem(
    val menuItemId: String,
    val quantity: Int,
    val isHalf: Boolean = false,
    val price: Double,
    val tableId: Int,
    val takeAway : Boolean = false
)