package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.OrderItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.domain.pricing.CartPricing
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

    suspend operator fun invoke(address: Address, paymentMethod: String? = null) {

        val cartItems = cartRepository.getAllCartItems()
        if (cartItems.isEmpty()) {
            throw IllegalStateException("Cart is empty")
        }

        val orderItems = cartItems.map { item ->
            // tableId "Takeaway" (ya null) => takeaway; warna dine-in with table
            val takeaway = item.tableId.isNullOrBlank() ||
                    item.tableId.equals("Takeaway", ignoreCase = true)
            OrderItem(
                name = item.name,
                quantity = item.quantity,
                price = item.unitPrice,
                orderType = if (takeaway) "takeaway" else "DINE_IN",
                tableNo = if (takeaway) null else item.tableId,
                isFull = item.portion == PortionType.FULL
            )
        }

        // Cart summary jaisa hi total (CartPricing = single source of truth)
        val total = CartPricing.total(CartPricing.subtotal(cartItems))

        val order = orderRepository.createOrder(
            items = orderItems,
            totalAmount = total,
            address = address
        )

        // Cart pe hi payment liya (Cash/UPI) to abhi record karo
        if (!paymentMethod.isNullOrBlank()) {
            orderRepository.setPaymentMethod(order.id, paymentMethod)
            // Takeaway: payment done -> ab sirf "Delivered" baaki (step 1)
            val orderIsTakeaway = orderItems.all { it.orderType.equals("takeaway", ignoreCase = true) }
            if (orderIsTakeaway) orderRepository.setFulfillmentStep(order.id, 1)
        }

        // ✅ 1. IMMEDIATE FIREBASE PUSH
        orderSyncRepository.syncSingleOrder(order.id)

        // ✅ 2. NOTIFICATION
        notificationHelper.showNewOrderNotification(order.orderNumber)

        cartRepository.clearCart()
    }
}



