package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.data.model.CartItem
import com.sample.restaurantordertakingapp.data.model.CartState

// Utils/CartUtils.kt
fun createSampleCartState(): CartState {
    val items = listOf(
        CartItem(
            id = "1",
            name = "Margherita Pizza",
            price = 12.00,
            quantity = 1,
            table = "Table 1",
            imageUrl = "",
            isFull = true,
            takeAway = true


            // Add more items as needed
        ),
        CartItem(
            id = "2",
            name = "Spicy Ramen",
            price = 9.50,
            quantity = 2,
             table = "Table 2",
            imageUrl = "",
            isFull = true,
            takeAway = true
        )
    )

    val subtotal = items.sumOf { it.price * it.quantity }
    val tax = subtotal * 0.05
    val total = subtotal + tax

    return CartState(
        items = items,
        subtotal = subtotal,
        tax = tax,
        total = total
    )
}

fun updateCartQuantity(cartState: CartState, itemId: String, newQuantity: Int): CartState {
    val updatedItems = cartState.items.map { item ->
        if (item.id == itemId) item.copy(quantity = newQuantity) else item
    }

    val subtotal = updatedItems.sumOf { it.price * it.quantity }
    val tax = subtotal * 0.05
    val total = subtotal + tax

    return cartState.copy(
        items = updatedItems,
        subtotal = subtotal,
        tax = tax,
        total = total
    )
}

fun removeItemFromCart(cartState: CartState, itemId: String): CartState {
    val updatedItems = cartState.items.filter { it.id != itemId }

    val subtotal = updatedItems.sumOf { it.price * it.quantity }
    val tax = subtotal * 0.05
    val total = subtotal + tax

    return cartState.copy(
        items = updatedItems,
        subtotal = subtotal,
        tax = tax,
        total = total
    )
}