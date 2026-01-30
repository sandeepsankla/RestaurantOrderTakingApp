package com.sample.restaurantordertakingapp.domain.repo

import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.domain.model.Order

interface OrderRepository {
    suspend fun createOrder(
        order: OrderEntity,
        address: AddressEntity,
        items: List<OrderItemEntity>
    )

    suspend fun getOrders(): List<Order>
}
