package com.sample.restaurantordertakingapp.data.repository


import com.sample.restaurantordertakingapp.data.local.LocalMenuDataSource
import com.sample.restaurantordertakingapp.data.mapper.menuDocumentToEntities
import com.sample.restaurantordertakingapp.data.mapper.toDomain
import com.sample.restaurantordertakingapp.data.remote.firebase.FirebaseMenuDataSource
import com.sample.restaurantordertakingapp.di.IoDispatcher
import com.sample.restaurantordertakingapp.domain.model.Menu
import com.sample.restaurantordertakingapp.domain.repo.MenuRepository
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject



class MenuRepositoryImpl @Inject constructor(
    private val remote: FirebaseMenuDataSource,
    private val local: LocalMenuDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MenuRepository {

    /*
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
 .addOnSuccessListener { snapshot ->
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
            }

        }.flowOn(Dispatchers.IO)
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
*/
    override fun fetchMenu(): Flow<Resource<Menu>> = flow {
        emit(Resource.Loading)

        // 1️⃣ Try local DB first
        val localMenu = withContext(dispatcher) {
            local.getMenuOnce()   // suspend fun (not Flow)
        }

        if (localMenu != null) {
            // ✅ Data available locally
            emit(Resource.Success(localMenu.toDomain()))
            return@flow
        }

        // 2️⃣ Fetch from Firestore
        val remoteMenu = withContext(dispatcher) {
            remote.fetchMenu()
        }

        // 3️⃣ Save to Room
        withContext(dispatcher) {
            val (menuEntity, categories, items) =
                remoteMenu.menuDocumentToEntities()

            local.insertFullMenu(
                menu = menuEntity,
                categories = categories,
                items = items
            )
        }

        // 4️⃣ Read again from Room (single source of truth)
        val savedMenu = withContext(dispatcher) {
            local.getMenuOnce()
        }

        if (savedMenu != null) {
            emit(Resource.Success(savedMenu.toDomain()))
        } else {
            emit(Resource.Error("Menu save failed"))
        }

    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }



}