package com.sample.restaurantordertakingapp.data.repository

import com.sample.restaurantordertakingapp.data.local.dao.CartDao
import com.sample.restaurantordertakingapp.data.mapper.toDomain
import com.sample.restaurantordertakingapp.data.mapper.toEntity
import com.sample.restaurantordertakingapp.di.IoDispatcher
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): CartRepository {
    override suspend fun addToCart(item: CartItem) =
        withContext(ioDispatcher) {
            cartDao.insertOrUpdate(item.toEntity())
        }

    override suspend fun updateQuantity(itemId: Int, qty: Int) =
        withContext(ioDispatcher) {
            cartDao.updateQuantity(itemId, qty)
        }

    override suspend fun removeItem(itemId: Int) =
        withContext(ioDispatcher) {
            cartDao.delete(itemId)
        }

    override fun observeCartItems(): Flow<List<CartItem>> =
        cartDao.observeCartItems()
            .map { list -> list.map { it.toDomain() } }
}