package com.sample.restaurantordertakingapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: String  = "",
    val menuItemId: Int = 0,
    val name: String = "",
    val quantity: Int =0,
    val unitPrice: Int  =0                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              ,
    val orderType: String  = "",                                                                                                                                                                        // DINE_IN / TAKEAWAY
    val tableNo: String?
)
