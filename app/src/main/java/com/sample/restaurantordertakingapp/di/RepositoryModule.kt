package com.sample.restaurantordertakingapp.di

import com.sample.restaurantordertakingapp.data.repository.CartRepositoryImpl
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}
