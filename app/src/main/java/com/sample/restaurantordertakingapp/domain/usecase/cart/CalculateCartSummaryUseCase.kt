package com.sample.restaurantordertakingapp.domain.usecase.cart

import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.CartSummary
import com.sample.restaurantordertakingapp.domain.pricing.CartPricing
import javax.inject.Inject

class CalculateCartSummaryUseCase @Inject constructor() {

    operator fun invoke(items: List<CartItem>): CartSummary {
        val subtotal = CartPricing.subtotal(items)
        val tax = CartPricing.tax(subtotal)
        val total = CartPricing.total(subtotal)

        return CartSummary(
            subtotal = subtotal.toDouble(),
            tax = tax.toDouble(),
            total = total.toDouble(),
            isEmpty = items.isEmpty()
        )
    }
}
