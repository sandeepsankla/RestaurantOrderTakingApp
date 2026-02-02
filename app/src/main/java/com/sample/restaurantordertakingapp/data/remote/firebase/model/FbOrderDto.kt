package com.sample.restaurantordertakingapp.data.remote.firebase.model

import com.sample.restaurantordertakingapp.domain.model.OrderStatus

data class FirebaseOrderDto(
    val orderId: String = "",
    val totalAmount: Int = 0,
    val status: OrderStatus = OrderStatus.CREATED,
    val createdAt: Long = 0L,
    val items: List<FirebaseOrderItemDto> = emptyList()
)

data class FirebaseOrderItemDto(
    val name: String = "",
    val quantity: Int = 0,
    val price: Int = 0,
    val orderType: String = "",
    val portion: String = ""
)
