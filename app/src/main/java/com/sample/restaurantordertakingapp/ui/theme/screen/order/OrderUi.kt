package com.sample.restaurantordertakingapp.ui.theme.screen.order

data class OrderUi(
    val id: String,
    val itemsText: String,   // "Chicken x2, Momos x1"
    val totalAmount: Int,
    val status: String
)
