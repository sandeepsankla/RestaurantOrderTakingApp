package com.sample.restaurantordertakingapp.data.mapper

import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderDto
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderItemDto
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.model.OrderItem

fun OrderWithItems.toDomain(): Order {
    return Order(
        id = order.orderId,
        items = items.map {
            OrderItem(
                name = it.itemName,
                quantity = it.quantity,
                price = it.price,
                orderType = it.orderType,
                isFull = it.isFull
            )
        },
        totalAmount = order.totalAmount,
        status = order.orderStatus,
        createdAt = order.createdAt
    )
}

fun Order.toFirebaseDto(): FirebaseOrderDto {
    return FirebaseOrderDto(
        orderId = id,
        totalAmount = totalAmount,
        status = status,
        createdAt = System.currentTimeMillis(),
        items = items.map {
            FirebaseOrderItemDto(
                name = it.name,
                quantity = it.quantity,
                price = it.price,
                orderType = it.orderType,
                portion = if (it.isFull) "Full" else "Half"
            )
        }
    )
}
fun Order.toOrderEntity(): OrderEntity {
    return OrderEntity(
        orderId = id,
        totalAmount = totalAmount,
        createdAt = createdAt,
        orderStatus = status
    )
}

fun Address.toAddressEntity(): AddressEntity {
    return AddressEntity(
        orderId = orderId,
        society = society,
        flatNo =flatNo,
        tower = tower,
        mobile = mobile
    )
}

fun Order.toOrderItemEntities(): List<OrderItemEntity> {
    return items.map { item ->
        OrderItemEntity(
            orderId = id,
            itemName = item.name,
            quantity = item.quantity,
            price = item.price,
            orderType = item.orderType,
            tableNo = item.orderType,
            isFull = item.isFull
        )
    }
}




