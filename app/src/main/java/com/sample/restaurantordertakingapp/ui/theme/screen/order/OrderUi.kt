package com.sample.restaurantordertakingapp.ui.theme.screen.order

import com.sample.restaurantordertakingapp.domain.model.OrderStatus


data class OrderUi(
    val orderId: String,
    val orderNumber: Int,
    val totalAmount: Int,
    val status: OrderStatus,
    val createdAt: Long,
    val itemsText: String
)
