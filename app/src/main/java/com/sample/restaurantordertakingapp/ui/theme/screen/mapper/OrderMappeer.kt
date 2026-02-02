package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrderUi

fun Order.toOrderUi(): OrderUi =
    OrderUi(
        orderId = id,
        orderNumber = orderNumber,
        totalAmount = totalAmount,
        status = status,
        createdAt = createdAt,
        itemsText = items.joinToString("\n") {
            "${it.name} x${it.quantity} (${it.orderType} - ${it.getPortion()})\""
        }
    )



