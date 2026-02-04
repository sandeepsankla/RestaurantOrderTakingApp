package com.sample.restaurantordertakingapp.data.repository

import com.google.firebase.firestore.ListenerRegistration
import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderItemDao
import com.sample.restaurantordertakingapp.data.mapper.toEntity
import com.sample.restaurantordertakingapp.data.mapper.toOrderEntity
import com.sample.restaurantordertakingapp.data.remote.firebase.FirebaseOrderListener
import com.sample.restaurantordertakingapp.di.IoDispatcher
import com.sample.restaurantordertakingapp.domain.repo.OrderPullRepository
import com.sample.restaurantordertakingapp.utils.NotificationHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderPullRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val firebaseListener: FirebaseOrderListener,
    private val notificationHelper: NotificationHelper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : OrderPullRepository {

    private var registration: ListenerRegistration? = null

    override fun startListening() {

        if (registration != null) return // already listening

        registration = firebaseListener.listen { firebaseOrder ->

            CoroutineScope(ioDispatcher).launch  {

                // 1️⃣ Avoid duplicates
                if (orderDao.orderExists(firebaseOrder.orderId) > 0) return@launch

                // 2️⃣ Convert Firebase → Room entities
                val orderEntity = firebaseOrder.toOrderEntity()
                val itemEntities =
                    firebaseOrder.items.map {
                        it.toEntity(firebaseOrder.orderId)
                    }

                // 3️⃣ Save to local DB
                orderDao.insertOrder(orderEntity)
                orderItemDao.insertItems(itemEntities)

                // 4️⃣ Notify staff
                notificationHelper.showNewOrderNotification(
                    orderNumber = orderEntity.orderNumber
                )
            }
        }
    }

    override fun stopListening() {
        registration?.remove()
        registration = null
    }
}