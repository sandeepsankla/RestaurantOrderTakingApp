package com.sample.restaurantordertakingapp.data.model

data class OrderItem(
    val id: String,
    val quantity: Int,
    val isFull: Boolean = true,
    val price: Double,
    val amount: Double,
    val tableId: Int,
    val takeAway : Boolean = false
)