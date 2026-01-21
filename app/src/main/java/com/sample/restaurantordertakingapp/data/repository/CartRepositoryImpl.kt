package com.sample.restaurantordertakingapp.data.repository

import com.sample.restaurantordertakingapp.data.local.dao.CartDao
import com.sample.restaurantordertakingapp.data.mapper.toDomain
import com.sample.restaurantordertakingapp.data.mapper.toEntity
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
): CartRepository {
    override fun observeCartItems(): Flow<List<CartItem>> {
        return cartDao.observeCartItems()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun addToCart(item: CartItem) {
        cartDao.insertOrUpdate(item.toEntity())
    }

    override suspend fun updateQuantity(itemId: String, quantity: Int) {
        cartDao.updateQuantity(itemId, quantity)
    }

    override suspend fun removeItem(itemId: String) {
        cartDao.deleteById(itemId)
    }


}