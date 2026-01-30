package com.sample.restaurantordertakingapp.data.repository

import androidx.room.Transaction
import com.sample.restaurantordertakingapp.data.local.dao.AddressDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderItemDao
import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.data.mapper.toAddressEntity
import com.sample.restaurantordertakingapp.data.mapper.toDomain
import com.sample.restaurantordertakingapp.data.mapper.toFirebaseDto
import com.sample.restaurantordertakingapp.data.mapper.toOrderEntity
import com.sample.restaurantordertakingapp.data.mapper.toOrderItemEntities
import com.sample.restaurantordertakingapp.data.remote.firebase.FirebaseMenuDataSource
import com.sample.restaurantordertakingapp.data.remote.firebase.FirebaseOrderDataSource
import com.sample.restaurantordertakingapp.di.IoDispatcher
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class OrderRepositoryImpl @Inject constructor(
    private val remote: FirebaseOrderDataSource,
    private val orderDao : OrderDao,
    private val addressDao : AddressDao,
    private val orderItemDao : OrderItemDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher): OrderRepository {


    @Transaction
    override suspend fun createOrder1(
        order: OrderEntity,
        address: AddressEntity,
        items: List<OrderItemEntity>
    ) {
        orderDao.insertOrder(order)
        addressDao.insertAddress(address)
        orderItemDao.insertOrderItems(items)

        // 2️⃣ Sync to Firebase

    }

    override suspend fun getOrders(): List<Order> =
         orderDao.getOrdersWithItems()
            .map { it.toDomain() }

    override suspend fun createOrder(order: Order, address: Address) {
        withContext(ioDispatcher){
            orderDao.insertOrder(order.toOrderEntity())
            addressDao.insertAddress(address.toAddressEntity())
            orderItemDao.insertOrderItems(order.toOrderItemEntities())

            remote.pushOrder(order.toFirebaseDto())
        }
    }


}
