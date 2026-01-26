package com.sample.restaurantordertakingapp.domain.usecase.cart

import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: CartRepository) {

    suspend operator fun invoke(item: CartItem) =
        repo.addToCart(item)
}