package com.sample.restaurantordertakingapp.data.repository

import androidx.room.Transaction
import com.sample.restaurantordertakingapp.data.local.dao.AddressDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderItemDao
import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderEntity
import com.sample.restaurantordertakingapp.data.local.entity.OrderItemEntity
import com.sample.restaurantordertakingapp.data.remote.firebase.FirebaseMenuDataSource
import com.sample.restaurantordertakingapp.di.IoDispatcher
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class OrderRepositoryImpl @Inject constructor(
    private val remote: FirebaseMenuDataSource,
    private val orderDao : OrderDao,
    private val addressDao : AddressDao,
    private val orderItemDao : OrderItemDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher): OrderRepository {


    @Transaction
    override suspend fun createOrder(
        order: OrderEntity,
        address: AddressEntity,
        items: List<OrderItemEntity>
    ) {
        orderDao.insertOrder(order)
        addressDao.insertAddress(address)
        orderItemDao.insertOrderItems(items)
    }
}
