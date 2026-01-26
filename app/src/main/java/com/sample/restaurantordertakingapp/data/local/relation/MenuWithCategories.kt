package com.sample.restaurantordertakingapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuEntity

/*
MenuEntitydata class MenuWithCategories(
    @Embedded val menu: MenuEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "menuId"
    )
    val categories: List<CategoryWithItems>
)*/
data class MenuWithCategories(
    @Embedded val menu: MenuEntity,
    @Relation(
        entity = CategoryEntity::class, // Room ko batana padega ki base entity kya hai
        parentColumn = "id",
        entityColumn = "menuId"
    )
    val categories: List<CategoryWithItems>
)