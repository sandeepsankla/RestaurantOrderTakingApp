package com.sample.restaurantordertakingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity

/*@Dao
interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getItemsForOrder(orderId: String): List<OrderItemEntity>
}*/

@Dao
interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItems(items: List<OrderItemEntity>)
}

