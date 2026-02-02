package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import javax.inject.Inject

class StartOrderSyncUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend operator fun invoke(
        onNewOrder: (Order) -> Unit
    ) {
        orderRepository.startOrderSync(onNewOrder)
    }
}

