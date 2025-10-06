package com.sample.restaurantordertakingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val menuItemId: String,
    val name: String,
    val price: Int,
    val quantity: Int,
    val isFull: Boolean,
    val imageUrl: String,
    val table:String? = null,
    val takeAway: Boolean = false,
)
