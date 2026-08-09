package com.sample.restaurantordertakingapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "addresses",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["orderId"])]
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: String = "",

    val society: String? = "",
    val flatNo: String? ="",
    val tower: String? ="",
    val mobile: String  = ""// ✅ mandatory
)

