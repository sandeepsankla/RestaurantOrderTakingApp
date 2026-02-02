package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend operator fun invoke(
        orderId: String,
        currentStatus: OrderStatus
    ) {
        val nextStatus = when (currentStatus) {
            OrderStatus.CREATED -> OrderStatus.READY
            OrderStatus.READY -> OrderStatus.DELIVERED
            OrderStatus.DELIVERED -> return // final state
        }

        orderRepository.updateOrderStatus(
            orderId = orderId,
            newStatus = nextStatus
        )
    }
}
