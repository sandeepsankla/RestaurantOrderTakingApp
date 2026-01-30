package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import java.util.UUID

class PlaceOrderUseCase(
    private val cartRepo: CartRepository,
    private val orderRepo: OrderRepository
) {

    suspend operator fun invoke(address: Address) {

        val cartItems : List<CartItem>  = cartRepo.getAllCartItems()

        val orderId = UUID.randomUUID().toString()

        val order = OrderEntity(
            orderId = orderId,
            totalAmount = cartItems.sumOf { it.totalPrice },
            createdAt = System.currentTimeMillis(),
            orderStatus = "CREATED"
        )

        val addressEntity = AddressEntity(
            orderId = orderId,
            society = address.society,
            flatNo = address.flatNo,
            tower = address.tower,
            mobile = address.mobile.toString()
        )

        val orderItems = cartItems.map {
            OrderItemEntity(
                orderId = orderId,
                menuItemId = it.menuItemId,
                name = it.name,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
                orderType =  if(it.tableId  ==  "takeaway") "Takeaway" else it.tableId ?: "takeAway",
                tableNo = it.tableId
            )
        }

        orderRepo.createOrder(order, addressEntity, orderItems)

        cartRepo.clearCart() // ✅ CRITICAL
    }
}

