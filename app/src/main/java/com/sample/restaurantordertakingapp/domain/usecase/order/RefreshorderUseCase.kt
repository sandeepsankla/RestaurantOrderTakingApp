package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.repo.OrderSyncRepository
import javax.inject.Inject

class RefreshOrdersUseCase @Inject constructor(
    private val orderSyncRepository: OrderSyncRepository
) {

    suspend operator fun invoke() {
       orderSyncRepository.syncOrders()
    }
}
