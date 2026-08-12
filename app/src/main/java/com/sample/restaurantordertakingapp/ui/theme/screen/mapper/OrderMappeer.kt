package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrderLineUi
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
        },
        items = items.map {
            OrderLineUi(
                name = it.name,
                quantity = it.quantity,
                orderType = it.orderType,
                isFull = it.isFull,
                tableNo = it.tableNo
            )
        },
        tandoorReady = tandoorReady,
        kitchenReady = kitchenReady,
        fulfillmentStep = fulfillmentStep,
        paymentMethod = paymentMethod,
        orderDate = orderDate
    )



