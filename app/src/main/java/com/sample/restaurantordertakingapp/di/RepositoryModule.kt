package com.sample.restaurantordertakingapp.di

import com.sample.restaurantordertakingapp.data.repository.CartRepositoryImpl
import com.sample.restaurantordertakingapp.data.repository.MenuRepositoryImpl
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.MenuRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
    @Binds
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository
}
