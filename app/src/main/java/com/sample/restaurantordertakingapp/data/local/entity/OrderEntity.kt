package com.sample.restaurantordertakingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String = "",
    val totalAmount: Int = 0,
    val createdAt: Long  = 0L,
    val orderStatus: String =""// CREATED, SYNCED
)
