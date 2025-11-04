package com.sample.restaurantordertakingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CartItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
