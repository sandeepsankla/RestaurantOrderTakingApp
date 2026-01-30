package com.sample.restaurantordertakingapp.domain.repo

import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity

interface OrderRepository {
    suspend fun createOrder(
        order: OrderEntity,
        address: AddressEntity,
        items: List<OrderItemEntity>
    )
}
