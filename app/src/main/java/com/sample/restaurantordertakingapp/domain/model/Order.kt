package com.sample.restaurantordertakingapp.domain.model


data class Order(
    val id: String,
    val items: List<OrderItem>,
    val totalAmount: Int,
    val status: String,
    val createdAt: Long
)

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Int,
    val orderType: String,
    val isFull: Boolean
){
    fun getPortion(): String {
        return if (isFull) "Full" else "Half"
    }
}


enum class OrderStatus {
    PLACED,
    COMPLETED
}
