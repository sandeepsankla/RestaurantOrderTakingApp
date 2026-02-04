package com.sample.restaurantordertakingapp.data.repository

import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.mapper.toDomain
import com.sample.restaurantordertakingapp.data.mapper.toFirebaseDto
import com.sample.restaurantordertakingapp.data.remote.firebase.FirebaseOrderDataSource
import com.sample.restaurantordertakingapp.domain.repo.OrderSyncRepository
import javax.inject.Inject

class OrderSyncRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val firebaseDataSource: FirebaseOrderDataSource
) : OrderSyncRepository {

    override suspend fun syncOrders() {

        val unsyncedOrders = orderDao.getUnsyncedOrdersWithItems()

        unsyncedOrders.forEach { orderWithItems ->

            val domainOrder = orderWithItems.toDomain()

            firebaseDataSource.pushOrder(
                domainOrder.toFirebaseDto()
            )

            // ✅ mark synced only after success
            orderDao.markOrderSynced(domainOrder.id)
        }
    }

    override suspend fun syncSingleOrder(orderId: String) {
        val orderWithItems = orderDao.getOrderWithItems(orderId)
            ?: return

        // ⛔ already synced → skip
        if (orderWithItems.order.isSynced) return

        firebaseDataSource.pushOrder(
            orderWithItems.toDomain().toFirebaseDto()
        )

        // ✅ mark synced AFTER successful push
        orderDao.markOrderSynced(orderId)
    }
}
