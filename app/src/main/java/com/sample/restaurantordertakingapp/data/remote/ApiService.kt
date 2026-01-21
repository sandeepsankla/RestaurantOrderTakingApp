package com.sample.restaurantordertakingapp.data.remote

import com.sample.restaurantordertakingapp.data.model.MenuResponse
import com.sample.restaurantordertakingapp.data.model.OrderRequest
import com.sample.restaurantordertakingapp.data.model.OrderResponse
import com.sample.restaurantordertakingapp.data.model.OrderStatus
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*
import retrofit2.http.POST

interface ApiService {



    @GET("menu.json")
    suspend fun getMenuResponse(): MenuResponse
    @GET("menu")
    fun getMenu(): Flow<Resource<MenuResponse>>

    @POST("orders")
    fun placeOrder(@Body orderRequest: OrderRequest): Flow<Resource<OrderResponse>>

    @GET("orders/table/{tableId}")
    fun getOrdersForTable(@Path("tableId") tableId: String): Flow<Resource<List<OrderStatus>>>
}