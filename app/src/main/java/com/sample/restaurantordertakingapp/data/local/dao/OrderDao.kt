package com.sample.restaurantordertakingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

/*@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE orderStatus = :status")
    suspend fun getOrdersByStatus(status: String): List<OrderEntity>

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Transaction
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    suspend fun getOrdersWithItems(): List<OrderWithItems>

    @Query("SELECT EXISTS(SELECT 1 FROM orders WHERE orderId = :id)")
    fun exists(id: String): Boolean
}*/


@Dao
interface OrderDao {

    /* ---------------------------------------------------------
    * ORDER NUMBER LOGIC (DAILY RESET, RACE SAFE)
    * --------------------------------------------------------- */

    @Query("""
        SELECT MAX(orderNumber)
        FROM orders
        WHERE orderDate = :orderDate
    """)
    suspend fun getLastOrderNumberForDate(orderDate: String): Int?


    /* ---------------------------------------------------------
     * INSERT ORDER
     * --------------------------------------------------------- */

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrder(order: OrderEntity)


    /* ---------------------------------------------------------
     * READ ORDERS (LATEST FIRST)
     * --------------------------------------------------------- */

    @Transaction
    @Query("""
        SELECT * FROM orders
        ORDER BY createdAt DESC
    """)
    fun observeOrdersWithItems(): Flow<List<OrderWithItems>>


    /* ---------------------------------------------------------
     * UPDATE STATUS (FORWARD ONLY)
     * --------------------------------------------------------- */

    @Query("""
        UPDATE orders
        SET status = :status
        WHERE orderId = :orderId
    """)
    suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    )

    @Transaction
    suspend fun insertOrderWithItems(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        orderItemDao: OrderItemDao
    ) {
        insertOrder(order)
        orderItemDao.insertItems(items)
    }


  /*  @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrder(order: OrderEntity)



    @Transaction
    @Query("""
        SELECT * FROM orders
        ORDER BY createdAt DESC
    """)
    fun observeOrdersWithItems(): Flow<List<OrderWithItems>>*/


}