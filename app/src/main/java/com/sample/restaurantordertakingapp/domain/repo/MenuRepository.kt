package com.sample.restaurantordertakingapp.domain.repo

import com.sample.restaurantordertakingapp.domain.model.Menu
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    /** Menu list (categories + items) */
    fun fetchMenu(): Flow<Resource<Menu>>

    /** Order placement (future use) */
    /*suspend fun submitOrder(
        tableId: String,
        items: List<OrderItem>
    ): Result<Unit>*/
}