package com.sample.restaurantordertakingapp.data.repository

import com.sample.restaurantordertakingapp.data.local.dao.AddressDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderDao
import com.sample.restaurantordertakingapp.data.local.dao.OrderItemDao
import com.sample.restaurantordertakingapp.data.local.relation.OrderWithItems
import com.sample.restaurantordertakingapp.data.mapper.toAddressEntity
import com.sample.restaurantordertakingapp.data.mapper.toDomain
import com.sample.restaurantordertakingapp.data.mapper.toEntity
import com.sample.restaurantordertakingapp.data.mapper.toFirebaseDto
import com.sample.restaurantordertakingapp.data.mapper.toOrderEntity
import com.sample.restaurantordertakingapp.di.IoDispatcher
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.model.Order
import com.sample.restaurantordertakingapp.domain.model.OrderItem
import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import com.sample.restaurantordertakingapp.utils.DateProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject


/*class OrderRepositoryImpl @Inject constructor(
    private val remote: FirebaseOrderDataSource,
    private val orderDao : OrderDao,
    private val addressDao : AddressDao,
    private val orderItemDao : OrderItemDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val fbOrderListener: FirebaseOrderListener,
    @ApplicationScope private val externalScope: CoroutineScope,
    ): OrderRepository {

    override suspend fun startOrderSync(onNewOrderSaved: (Order) -> Unit) {
        fbOrderListener.listenForOrders { firebaseDto ->
            externalScope.launch {
                val order = firebaseDto.toDomain()

                if (orderDao.exists(order.id)) return@launch

                withContext(ioDispatcher) {
                    orderDao.insertOrder(order.toOrderEntity())
                    onNewOrderSaved(order)
                }
            }
        }
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


}*/

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val addressDao: AddressDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : OrderRepository {
    override suspend fun updateOrderStatus(
        orderId: String,
        newStatus: OrderStatus
    ) {
        orderDao.updateOrderStatus(
            orderId = orderId,
            status = newStatus
        )
    }

    override fun observeOrdersWithItems(): Flow<List<OrderWithItems>> {
        return orderDao.observeOrdersWithItems()
    }

    override suspend fun createOrder(
        items: List<OrderItem>,
        totalAmount: Int,
        address: Address
    ): Order = withContext(ioDispatcher) {

        val orderId = UUID.randomUUID().toString()
        val orderDate = DateProvider.today()

        val nextOrderNo =
            (orderDao.getLastOrderNumberForDate(orderDate) ?: 0) + 1

        val order = Order(
            id = orderId,
            orderNumber = nextOrderNo,
            orderDate = orderDate,
            totalAmount = totalAmount,
            status = OrderStatus.CREATED,
            createdAt = System.currentTimeMillis(),
            items = items
        )

        // 🔒 Atomic transaction
        orderDao.insertOrderWithItems(
            order = order.toOrderEntity(),
            items = items.map { it.toEntity(orderId) },
            orderItemDao = orderItemDao
        )

        addressDao.insertAddress(
            address.toEntity(orderId)
        )

        order
    }



    override fun observeOrders(): Flow<List<Order>> =
        orderDao.observeOrdersWithItems()
            .map { list -> list.map { it.toDomain() } }

    override fun startOrderSync(onNewOrder: (Order) -> Unit) {

    }
}
