package com.sample.restaurantordertakingapp.domain.usecase.cart

import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import javax.inject.Inject

class RemoveItemUseCase @Inject constructor(private val repo: CartRepository) {
    suspend operator fun invoke(itemId: Int) =
        repo.removeItem(itemId)
}