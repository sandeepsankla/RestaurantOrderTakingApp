package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.OrderItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import com.sample.restaurantordertakingapp.domain.repo.OrderSyncRepository
import com.sample.restaurantordertakingapp.utils.NotificationHelper
import javax.inject.Inject

/*class PlaceOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) {

    suspend operator fun invoke(address: Address) {

        val cartItems = cartRepository.getAllCartItems()
        if (cartItems.isEmpty()) {
            throw IllegalStateException("Cart is empty")
        }

        val orderItems = cartItems.map {
            OrderItem(
                name = it.name,
                quantity = it.quantity,
                price = it.unitPrice,
                orderType = it.tableId?: "takeaway",
                tableNo = it.tableId,
                isFull = it.portion== PortionType.FULL
            )
        }

        val total = cartItems.sumOf { it.totalPrice }

        orderRepository.createOrder(
            items = orderItems,
            totalAmount = total,
            address= address
        )

        cartRepository.clearCart()
    }
}*/
class PlaceOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val orderSyncRepository: OrderSyncRepository,
    private val notificationHelper: NotificationHelper
) {

    suspend operator fun invoke(address: Address) {

        val cartItems = cartRepository.getAllCartItems()
        if (cartItems.isEmpty()) {
            throw IllegalStateException("Cart is empty")
        }

        val orderItems = cartItems.map {
            OrderItem(
                name = it.name,
                quantity = it.quantity,
                price = it.unitPrice,
                orderType = it.tableId?: "takeaway",
                tableNo = it.tableId,
                isFull = it.portion== PortionType.FULL
            )
        }

        val total = cartItems.sumOf { it.totalPrice }

     val order =    orderRepository.createOrder(
            items = orderItems,
            totalAmount = total,
            address= address
        )

        // ✅ 1. IMMEDIATE FIREBASE PUSH
        orderSyncRepository.syncSingleOrder(order.id)

        // ✅ 2. NOTIFICATION
        notificationHelper.showNewOrderNotification(order.orderNumber)

        cartRepository.clearCart()
    }
}



