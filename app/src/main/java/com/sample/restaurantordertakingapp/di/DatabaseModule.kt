package com.sample.restaurantordertakingapp.di

import android.content.Context
import androidx.room.Room
import com.sample.restaurantordertakingapp.data.local.AppDatabase
import com.sample.restaurantordertakingapp.data.local.dao.AddressDao
import com.sample.restaurantordertakingapp.data.local.dao.CartDao
import com.sample.restaurantordertakingapp.data.local.dao.MenuDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvider {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    fun provideMenuDao(db: AppDatabase): MenuDao =
        db.menuDao()

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao =
        db.orderDao()

    @Provides
    fun provideAddressDao(db: AppDatabase): AddressDao =
        db.addressDao()

    @Provides
    fun provideOrderItemDao(db: AppDatabase): OrderItemDao =
        db.orderItemDao()

}