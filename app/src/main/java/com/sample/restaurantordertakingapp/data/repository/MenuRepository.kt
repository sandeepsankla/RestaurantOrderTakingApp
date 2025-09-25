package com.sample.restaurantordertakingapp.data.repository

import com.sample.restaurantordertakingapp.data.api.ApiService
import com.sample.restaurantordertakingapp.data.model.Menu
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.data.model.OrderItem
import com.sample.restaurantordertakingapp.data.model.OrderRequest
import com.sample.restaurantordertakingapp.data.model.OrderResponse
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuRepository @Inject constructor(private val api: ApiService) {

    fun fetchMenu(): Flow<Resource<List<MenuItem>>> =
        api.getMenu()
            .map { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(resource.data.menu.categories.flatMap { it.items })
                    is Resource.Error -> Resource.Error(resource.message)
                    Resource.Loading -> Resource.Loading
                }
            }

    fun submitOrder(tableId: String, items: List<OrderItem>): Flow<Resource<OrderResponse>> =
        api.placeOrder(OrderRequest(tableId, items))


    fun fetchMenuFromJson(): Flow<Resource<Menu>> {
        return flow {
            emit(Resource.Loading)
            try {
                val resp = api.getMenuResponse()
                // Now resp.menu.categories.flatMap items
                //val listItems = resp.menu.categories.flatMap { it.items }
                emit(Resource.Success(resp.menu))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Error fetching menu"))
            }
        }.flowOn(Dispatchers.IO)
    }



}