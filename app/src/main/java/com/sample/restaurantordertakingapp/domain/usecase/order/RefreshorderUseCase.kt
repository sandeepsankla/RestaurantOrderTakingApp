package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import javax.inject.Inject

class RefreshOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend operator fun invoke() {
        // Firebase sync will be added here in STEP 6
    }
}
