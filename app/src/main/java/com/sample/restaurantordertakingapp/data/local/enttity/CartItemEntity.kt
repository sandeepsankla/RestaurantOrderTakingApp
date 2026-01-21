package com.sample.restaurantordertakingapp.data.local.enttity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val menuItemId: String,
    val name: String,
    val fullPrice: Int,   // unit price
    val halfPrice: Int,   // unit price
    val quantity: Int,
    val isFull: Boolean,
    val imageUrl: String? = null,
    val tableId:String? = null,
    val takeAway: Boolean = false,
)