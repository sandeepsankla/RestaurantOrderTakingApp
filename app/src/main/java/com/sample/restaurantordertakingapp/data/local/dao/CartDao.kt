package com.sample.restaurantordertakingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.restaurantordertakingapp.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // 🔹 Observe full cart (reactive)
    @Query("SELECT * FROM cart_items")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    // 🔹 Insert new item OR update existing one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartItemEntity)

    // 🔹 Update quantity (called from ViewModel)
    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :itemId")
    suspend fun updateQuantity(itemId: Int, quantity: Int)

    // 🔹 Remove single item
    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun delete(itemId: Int)

    // 🔹 Optional: Clear cart (checkout / cancel)
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // 🔹 Optional: Cart count (reactive)
    @Query("SELECT IFNULL(SUM(quantity), 0) FROM cart_items")
    fun observeCartCount(): Flow<Int>

}