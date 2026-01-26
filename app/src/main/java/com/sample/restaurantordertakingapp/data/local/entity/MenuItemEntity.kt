package com.sample.restaurantordertakingapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "menu_item",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MenuItemEntity(
    @PrimaryKey val id: String,
    val categoryId: Int,
    val name: String,
    val halfPrice: Int,
    val fullPrice: Int,
    val description: String?,
    val imageUrl: String?
)
