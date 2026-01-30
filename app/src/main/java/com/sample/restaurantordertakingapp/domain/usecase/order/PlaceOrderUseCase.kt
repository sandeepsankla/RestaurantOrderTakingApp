package com.sample.restaurantordertakingapp.domain.usecase.order


import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.model.OrderItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import java.util.UUID

class PlaceOrderUseCase(
    private val cartRepo: CartRepository,
    private val orderRepo: OrderRepository
) {

    suspend operator fun invoke(address: Address) {

        val cartItems = cartRepo.getAllCartItems()
        val orderId = UUID.randomUUID().toString()

        val order = Order(
            id = orderId,
            totalAmount = cartItems.sumOf { it.totalPrice },
            status = "CREATED",
            createdAt = System.currentTimeMillis(),
            items = cartItems.map {
                OrderItem(
                    name = it.name,
                    quantity = it.quantity,
                    price = it.unitPrice,
                    orderType =
                        if (it.tableId == "takeaway") "Takeaway"
                        else it.tableId ?: "Takeaway",
                    isFull = it.portion == PortionType.FULL
                )
            }
        )
        address.orderId = orderId
        orderRepo.createOrder(order, address)

        cartRepo.clearCart() // ✅ correct place
    }
}

