package com.sample.restaurantordertakingapp.data.repository

import android.util.Log
import com.sample.restaurantordertakingapp.data.local.CartDao
import com.sample.restaurantordertakingapp.data.local.CartItemEntity
import com.sample.restaurantordertakingapp.data.model.CartItem
import com.sample.restaurantordertakingapp.data.model.CartState
import com.sample.restaurantordertakingapp.utils.toCartItem
import com.sample.restaurantordertakingapp.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
        // Get all cart items as Flow for real-time updates
        fun getCartItems(): Flow<List<CartItem>> =
            cartDao.getAllCartItems().map { entities ->
                entities.map { it.toCartItem() }
            }

        // Get calculated CartState as Flow
        fun getCartState(): Flow<CartState> =
            cartDao.getAllCartItems().map { entities ->
                val items = entities.map { it.toCartItem() }
                calculateCartState(items)
            }

        suspend fun addToCart(item: CartItem) {
            cartDao.insert(item.toEntity())
        }

        suspend fun updateQuantity(productId: String, quantity: Int) {
            // First get current item, then update
            val currentItems = cartDao.getAllCartItems().first()
            val item = currentItems.find { it.menuItemId == productId }
            item?.let {
                cartDao.insert(it.copy(quantity = quantity))
            }
        }

        suspend fun removeFromCart(productId: String) {
            val currentItems = cartDao.getAllCartItems().first()
            val item = currentItems.find { it.menuItemId == productId }
            item?.let { cartDao.deleteItem(it.menuItemId) }
        }

        private fun calculateCartState(items: List<CartItem>): CartState {
            val subtotal = items.sumOf { it.price * it.quantity }
            val tax = subtotal * 0.05 // 5% GST
            val total = subtotal + tax

            return CartState(
                items = items,
                subtotal = subtotal,
                tax = tax,
                total = total
            )
        }

    suspend fun getCartItemsCount(): Flow<Int> = cartDao.getCartItemCount()

}