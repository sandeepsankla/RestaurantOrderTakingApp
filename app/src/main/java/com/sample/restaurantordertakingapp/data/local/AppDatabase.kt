package com.sample.restaurantordertakingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.restaurantordertakingapp.data.local.dao.CartDao
import com.sample.restaurantordertakingapp.data.local.dao.MenuDao
import com.sample.restaurantordertakingapp.data.local.entity.CartItemEntity
import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuItemEntity


@Database(entities = [CartItemEntity::class, MenuEntity::class,
    CategoryEntity::class, MenuItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun menuDao(): MenuDao
}
