package com.sample.restaurantordertakingapp.ui.theme.screen.order

import com.sample.restaurantordertakingapp.domain.model.OrderStatus


data class OrderUi(
    val orderId: String,
    val orderNumber: Int,
    val totalAmount: Int,
    val status: OrderStatus,
    val createdAt: Long,
    val itemsText: String,
    val items: List<OrderLineUi> = emptyList(),
    val tandoorReady: Boolean = false,
    val kitchenReady: Boolean = false,
    val fulfillmentStep: Int = 0,  // 0 = new, 1 = step-1 done, 2 = completed
    val paymentMethod: String? = null,   // CASH / UPI / UDHAAR
    val orderDate: String = ""           // yyyy-MM-dd (report grouping)
) {
    /** Koi bhi item table pe hai to ye table order hai. */
    val isTakeaway: Boolean
        get() = items.isNotEmpty() && items.all { it.orderType.equals("takeaway", ignoreCase = true) }

    val tableLabel: String?
        get() = items.firstOrNull { !it.tableNo.isNullOrBlank() }?.tableNo
}

data class OrderLineUi(
    val name: String,
    val quantity: Int,
    val orderType: String,
    val isFull: Boolean,
    val tableNo: String? = null
)
