package com.sample.restaurantordertakingapp.di

import android.content.Context
import androidx.room.Room
import com.sample.restaurantordertakingapp.data.local.AppDatabase

object DatabaseProvider {
    fun create(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }
}