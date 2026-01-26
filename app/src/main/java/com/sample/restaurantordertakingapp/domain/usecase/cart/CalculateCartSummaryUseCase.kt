package com.sample.restaurantordertakingapp.domain.usecase.cart

import CartSummary
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import javax.inject.Inject


class CalculateCartSummaryUseCase @Inject constructor() {
operator fun invoke(items: List<CartItem>): CartSummary {
    val subtotal = items.sumOf { item ->
        val unitPrice =
            if (item.portion == PortionType.FULL)
                item.fullPrice
            else
                item.halfPrice

        unitPrice * item.quantity
    }.toDouble()

    val tax = subtotal * 0.05
    val total = subtotal + tax

    return CartSummary(
        subtotal = subtotal,
        tax = tax,
        total = total,
        isEmpty = items.isEmpty()
    )
}
}