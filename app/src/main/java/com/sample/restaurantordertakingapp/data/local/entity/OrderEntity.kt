package com.sample.restaurantordertakingapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sample.restaurantordertakingapp.domain.model.OrderStatus

/*@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String = "",
    val totalAmount: Int = 0,
    val createdAt: Long  = 0L,
    val orderStatus: String =""// CREATED, SYNCED
)*/
@Entity(
    tableName = "orders",
    indices = [
        Index(value = ["orderDate", "orderNumber"], unique = true)
    ]
)
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val orderNumber: Int,
    val orderDate: String,          // yyyy-MM-dd
    val totalAmount: Int,
    val status: OrderStatus,
    val createdAt: Long
)
