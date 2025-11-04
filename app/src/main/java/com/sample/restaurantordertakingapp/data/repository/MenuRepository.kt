package com.sample.restaurantordertakingapp.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.sample.restaurantordertakingapp.data.api.ApiService
import com.sample.restaurantordertakingapp.data.model.CartItem
import com.sample.restaurantordertakingapp.data.model.Menu
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.data.model.MenuResponse
import com.sample.restaurantordertakingapp.data.model.OrderItem
import com.sample.restaurantordertakingapp.data.model.OrderRequest
import com.sample.restaurantordertakingapp.data.model.OrderResponse
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject



class MenuRepository @Inject constructor(private val api: ApiService) {

    fun fetchMenu(): Flow<Resource<List<MenuItem>>> =
        api.getMenu()
            .map { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(resource.data.menu!!.categories.flatMap { it.items })
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
                emit(Resource.Success(resp.menu!!))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Error fetching menu"))
            }
        }.flowOn(Dispatchers.IO)
    }



    fun fetchMenusFromFireStore(): Flow<Resource<Menu>> {
        return flow {
            //   var isSuccess : Boolean = false
            //    var errorMessage : String = ""
            emit(Resource.Loading)
            try {
                val firestore = Firebase.firestore
                val snapshot = firestore.collection("menus")
                    .document("default")  // or your document ID
                    .get()
                    .await()
                val menuResponse = snapshot.toObject(Menu::class.java)

                if (menuResponse != null) {
                    // Assuming MenuResponse has a non-null menu field:
                    emit(Resource.Success(menuResponse))
                } else {
                    emit(Resource.Error("Menu not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Error fetching menu"))
            }
            /* .addOnSuccessListener { snapshot ->
                        isSuccess = true
                        menuResponse = snapshot.toObject(MenuResponse::class.java)
                    }
                    .addOnFailureListener { e ->
                        isSuccess = false
                        errorMessage = e.localizedMessage
                    }
                    .await()
                if(isSuccess && menuResponse != null){
                    emit(Resource.Success(menuResponse!!.menu!!))
                } else {
                    emit(Resource.Error(errorMessage))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Error fetching menu"))
            }*/
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addToCart(item: CartItem) {

    }





    suspend fun uploadMenuJson(context: Context) {
        val json = context.assets.open("menu.json").bufferedReader().use { it.readText() }
        val menuResponse = Gson().fromJson(json, MenuResponse::class.java)

        val firestore = Firebase.firestore
        val menuDoc = firestore.collection("menus").document("default")

        // Optionally break into subcollections
        val dataMap = mapOf(
            "categories" to menuResponse.menu?.categories?.map { category ->
                mapOf(
                    "id" to category.id,
                    "name" to category.name,
                    "items" to category.items.map { item ->
                        mapOf(
                            "id" to item.id,
                            "name" to item.name,
                            "description" to item.description,
                            "price" to item.price,
                            "imageUrl" to item.imageUrl
                        )
                    }
                )
            }
        )

        menuDoc.set(dataMap)
            .addOnSuccessListener { Log.d("sasa", "Menu uploaded to Firestore") }
            .addOnFailureListener { e -> Log.e("sasa", "Failed to upload", e) }
    }


}