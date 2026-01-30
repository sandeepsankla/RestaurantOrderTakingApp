package com.sample.restaurantordertakingapp.domain.repo

import com.sample.restaurantordertakingapp.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(): Flow<List<CartItem>>
    suspend fun getAllCartItems(): List<CartItem>
    suspend fun addToCart(item: CartItem)
    suspend fun updateQuantity(itemId: Int, quantity: Int)
    suspend fun removeItem(itemId: Int)
    suspend fun clearCart()
}