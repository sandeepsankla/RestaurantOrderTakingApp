package com.sample.restaurantordertakingapp.domain.usecase.order

import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
   suspend operator fun invoke(): List<Order> = repository.getOrders()

}
