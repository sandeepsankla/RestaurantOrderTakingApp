package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrderUi

fun Order.toOrderUi(): OrderUi {
    return OrderUi(
        id = id,
        totalAmount = totalAmount,
        status = status,
        itemsText = items.joinToString { item ->
            "${item.name} x${item.quantity}"
        }
    )
}



