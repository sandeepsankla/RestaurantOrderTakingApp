package com.sample.restaurantordertakingapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuItemEntity

data class CategoryWithItems(
    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val items: List<MenuItemEntity>
)
