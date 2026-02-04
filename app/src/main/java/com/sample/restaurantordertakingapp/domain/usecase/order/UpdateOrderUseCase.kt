package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import com.sample.restaurantordertakingapp.domain.repo.OrderSyncRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val orderSyncRepository: OrderSyncRepository
) {

    suspend operator fun invoke(
        orderId: String,
        currentStatus: OrderStatus
    ) {
        val nextStatus = when (currentStatus) {
            OrderStatus.CREATED -> OrderStatus.READY
            OrderStatus.READY -> OrderStatus.DELIVERED
            OrderStatus.DELIVERED -> return
        }

        // ✅ Local update marks isSynced = 0
        orderRepository.updateOrderStatus(orderId, nextStatus)

        // 2️⃣ Infra sync
        orderSyncRepository.syncSingleOrder(orderId)
    }
}
