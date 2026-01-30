package com.sample.restaurantordertakingapp.domain.model


data class Order(
    val id: String,
    val items: List<OrderItem>,
    val totalAmount: Int,
    val status: String
)

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Int
)


enum class OrderStatus {
    PLACED,
    COMPLETED
}
