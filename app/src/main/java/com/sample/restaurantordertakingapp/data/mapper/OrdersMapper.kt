package com.sample.restaurantordertakingapp.data.mapper

import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.model.OrderItem

fun OrderWithItems.toDomain(): Order {
    return Order(
        id = order.orderId,
        totalAmount = order.totalAmount,
        status = order.orderStatus,
        items = items.map {
            OrderItem(
                name = it.itemName,
                quantity = it.quantity,
                price = it.price,
                orderType = it.orderType,
                isFull = it.isFull
            )
        }
    )
}

