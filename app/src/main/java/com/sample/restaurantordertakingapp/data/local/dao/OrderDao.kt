package com.sample.restaurantordertakingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE orderStatus = :status")
    suspend fun getOrdersByStatus(status: String): List<OrderEntity>

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)
}
