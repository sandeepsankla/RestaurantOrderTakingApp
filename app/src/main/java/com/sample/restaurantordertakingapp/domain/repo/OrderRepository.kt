package com.sample.restaurantordertakingapp.domain.repo

import androidx.room.Query
import androidx.room.Transaction
import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.model.OrderItem
import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {


    fun observeOrdersWithItems(): Flow<List<OrderWithItems>>
    suspend fun updateOrderStatus(
        orderId: String,
        newStatus: OrderStatus
    )
    suspend fun createOrder(
        items: List<OrderItem>,
        totalAmount: Int,
        address: Address
    ): Order

    fun observeOrders(): Flow<List<Order>>
    fun startOrderSync(onNewOrder: (Order) -> Unit)


    /* suspend fun startOrderSync(
         onNewOrderSaved: (Order) -> Unit
     )*/
}
