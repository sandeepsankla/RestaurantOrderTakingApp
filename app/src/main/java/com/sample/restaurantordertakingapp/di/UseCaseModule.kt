package com.sample.restaurantordertakingapp.di

import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.usecase.cart.AddToCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.CalculateCartSummaryUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.ObserveCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.RemoveItemUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.UpdateQuantityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddToCartUseCase(
        cartRepository: CartRepository
    ): AddToCartUseCase {
        return AddToCartUseCase(cartRepository)
    }

    @Provides
    fun provideAObserveCartUseCase(
        cartRepository: CartRepository
    ): ObserveCartUseCase {
        return ObserveCartUseCase(cartRepository)
    }

    @Provides
    fun provideRemoveItemUseCase(
        cartRepository: CartRepository
    ): RemoveItemUseCase {
        return RemoveItemUseCase(cartRepository)
    }

    @Provides
    fun provideUpdateQuantityUseCase(
        cartRepository: CartRepository
    ): UpdateQuantityUseCase {
        return UpdateQuantityUseCase(cartRepository)
    }


    @Provides
    fun provideCalculateCartSummaryUseCase(): CalculateCartSummaryUseCase =
        CalculateCartSummaryUseCase()
}
