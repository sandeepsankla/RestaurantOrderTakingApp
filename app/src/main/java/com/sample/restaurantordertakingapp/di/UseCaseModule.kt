package com.sample.restaurantordertakingapp.di

import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import com.sample.restaurantordertakingapp.domain.usecase.cart.AddToCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.CalculateCartSummaryUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.ObserveCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.RemoveItemUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.UpdateQuantityUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.ObserveOrdersUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.PlaceOrderUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.UpdateOrderStatusUseCase
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
    fun providePlaceOrderUseCase(
        cartRepo: CartRepository,
        orderRepo: OrderRepository

    ): PlaceOrderUseCase {
        return PlaceOrderUseCase(cartRepo, orderRepo)
    }


    @Provides
    fun provideCalculateCartSummaryUseCase(): CalculateCartSummaryUseCase =
        CalculateCartSummaryUseCase()

    @Provides
    fun provideObserveOrdersUseCase(
        orderRepository: OrderRepository
    ): ObserveOrdersUseCase =
        ObserveOrdersUseCase(orderRepository)

    @Provides
    fun provideUpdateOrderStatusUseCase(
        orderRepository: OrderRepository
    ): UpdateOrderStatusUseCase =
        UpdateOrderStatusUseCase(orderRepository)
}
