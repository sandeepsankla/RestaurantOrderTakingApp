package com.sample.restaurantordertakingapp.domain.model


data class Order(
    val id: String,              // UUID
    val orderNumber: Int,         // 1,2,3 (daily)
    val orderDate: String,        // yyyy-MM-dd
    val totalAmount: Int,
    val status: OrderStatus,
    val createdAt: Long,
    val items: List<OrderItem>
)
data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Int,
    val orderType: String,   // TAKEAWAY / DINE_IN
    val tableNo: String?,    // null for takeaway
    val isFull: Boolean
){
    fun getPortion(): String {
        return if (isFull) "Full" else "Half"
    }
}

