package com.sample.restaurantordertakingapp.domain.pricing

import com.sample.restaurantordertakingapp.domain.model.CartItem
import kotlin.math.roundToInt

/**
 * Cart/order money math ka SINGLE source of truth.
 *
 * Cart summary (jo customer ko dikhta hai) aur order placement (jo actually
 * charge hota hai) — dono yahi use karte hain, taaki dono kabhi mismatch na hon.
 *
 * Tax abhi 0 hai (yaani total = subtotal, jo pehle bhi actually charge ho raha tha).
 * Agar tax chahiye to sirf [TAX_RATE] badlo — dono jagah apne aap apply ho jayega.
 */
object CartPricing {

    /** e.g. 0.05 = 5% tax. 0.0 = koi tax nahi. */
    const val TAX_RATE: Double = 0.0

    fun subtotal(items: List<CartItem>): Int =
        items.sumOf { it.totalPrice }

    fun tax(subtotal: Int): Int =
        (subtotal * TAX_RATE).roundToInt()

    fun total(subtotal: Int): Int =
        subtotal + tax(subtotal)
}
