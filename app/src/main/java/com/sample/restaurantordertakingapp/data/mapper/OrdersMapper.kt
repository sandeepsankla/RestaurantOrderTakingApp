package com.sample.restaurantordertakingapp.data.mapper

import android.annotation.SuppressLint
import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderDto
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderItemDto
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.model.OrderItem
import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import com.sample.restaurantordertakingapp.utils.DateProvider
import java.text.SimpleDateFormat
import java.util.Date

fun Order.toOrderEntity(): OrderEntity =
    OrderEntity(
        orderId = id,
        orderNumber = orderNumber,
        orderDate = orderDate,
        totalAmount = totalAmount,
        status = status,
        createdAt = createdAt
    )

fun OrderItem.toEntity(orderId: String): OrderItemEntity =
    OrderItemEntity(
        orderId = orderId,
        name = name,
        quantity = quantity,
        price = price,
        orderType = orderType,
        tableNo = tableNo,
        isFull = isFull
    )


fun Order.toFirebaseDto(): FirebaseOrderDto {
    return FirebaseOrderDto(
        orderId = id,
        totalAmount = totalAmount,
        status = status.name,   // enum → String
        createdAt = createdAt,
        items = items.map {
            FirebaseOrderItemDto(
                name = it.name,
                quantity = it.quantity,
                price = it.price,
                orderType = it.orderType,
                portion = if (it.isFull) "FULL" else "HALF"
            )
        }
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
            name = item.name,
            quantity = item.quantity,
            price = item.price,
            orderType = item.orderType,
            tableNo = item.orderType,
            isFull = item.isFull
        )
    }
}
/*
fun FirebaseOrderDto.toDomain(): Order {
    return Order(
        id = orderId,
        totalAmount = totalAmount,
        status = status,
        createdAt = createdAt,
        items = items.map {
            OrderItem(
                name = it.name,
                quantity = it.quantity,
                price = it.price,
                orderType = it.orderType,
                isFull = it.portion == "Full"
            )
        }
    )
}
*/

fun OrderWithItems.toDomain(): Order =
    Order(
        id = order.orderId,
        orderNumber = order.orderNumber,
        orderDate = order.orderDate,
        totalAmount = order.totalAmount,
        status = order.status,
        createdAt = order.createdAt,
        items = items.map {
            OrderItem(
                name = it.name,
                quantity = it.quantity,
                price = it.price,
                orderType = it.orderType,
                tableNo = it.tableNo,
                isFull = it.isFull
            )
        }
    )


@SuppressLint("SimpleDateFormat")
fun FirebaseOrderDto.toOrderEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        orderNumber = orderNumber,   // ✅ USE SAME NUMBER
        totalAmount = totalAmount,
        status = OrderStatus.valueOf(status),
        createdAt = createdAt,
        isSynced = true,
        orderDate = SimpleDateFormat("yyyy-MM-dd").format(Date(createdAt))
    )
}


fun FirebaseOrderItemDto.toEntity(orderId: String): OrderItemEntity {
    return OrderItemEntity(
        orderId = orderId,
        name = name,
        quantity = quantity,
        price = price,
        orderType = orderType,
        tableNo = null,
        isFull = portion == "FULL"
    )
}





