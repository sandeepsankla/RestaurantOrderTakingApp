package com.sample.restaurantordertakingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.restaurantordertakingapp.data.local.dao.AddressDao
import com.sample.restaurantordertakingapp.data.local.dao.CartDao
import com.sample.restaurantordertakingapp.data.local.dao.MenuDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderItemDao
import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.CartItemEntity
import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuItemEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity


@Database(entities = [CartItemEntity::class, MenuEntity::class,
    CategoryEntity::class, MenuItemEntity::class, AddressEntity::class,
    OrderEntity::class, OrderItemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun menuDao(): MenuDao
    abstract fun addressDao(): AddressDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
}
