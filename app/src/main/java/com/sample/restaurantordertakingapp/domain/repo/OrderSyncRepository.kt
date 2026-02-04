package com.sample.restaurantordertakingapp.domain.repo

interface OrderSyncRepository {
    suspend fun syncOrders()
     suspend fun syncSingleOrder(orderId: String)
}