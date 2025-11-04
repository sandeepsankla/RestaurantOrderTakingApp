package com.sample.restaurantordertakingapp.data.repository

import android.util.Log
import com.sample.restaurantordertakingapp.data.local.CartDao
import com.sample.restaurantordertakingapp.data.local.CartItemEntity
import com.sample.restaurantordertakingapp.data.model.CartItem
import com.sample.restaurantordertakingapp.utils.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllCartItems()

    suspend fun addToCart(item: CartItem) {
        val entity = item.toEntity()
      //val rowId =   cartDao.insert(entity)
       // Log.d("sasa","cartitem added$rowId")
    }

    suspend fun clearCart() {
       // cartDao.clearAll()
    }

    // other methods...
}