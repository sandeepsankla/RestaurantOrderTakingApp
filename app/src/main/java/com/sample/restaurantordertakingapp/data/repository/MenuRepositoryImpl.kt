package com.sample.restaurantordertakingapp.data.repository


import com.sample.restaurantordertakingapp.data.local.LocalMenuDataSource
import com.sample.restaurantordertakingapp.data.local.pref.MenuVersionStore
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
    private val versionStore: MenuVersionStore,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MenuRepository {

    override fun fetchMenu(): Flow<Resource<Menu>> = flow {
        emit(Resource.Loading)
        val localVersion = versionStore.getLocalVersion()

        var remoteVersion: Int? = null
        try {
            remoteVersion = remote.fetchMenuVersion()
        } catch (e: Exception) {
            // Offline / network failure -> try loading local DB cache
            val localMenu = local.getMenuOnce()
            if (localMenu != null) {
                emit(Resource.Success(localMenu.toDomain()))
                return@flow
            } else {
                emit(Resource.Error(e.localizedMessage ?: "Offline and no cached menu available"))
                return@flow
            }
        }

        // 1️⃣ Same version -> load from Room
        if (localVersion == remoteVersion) {
            val localMenu = local.getMenuOnce()
            if (localMenu != null) {
                emit(Resource.Success(localMenu.toDomain()))
                return@flow
            }
        }

        // 2️⃣ Fetch fresh menu from Firestore
        val remoteMenu = withContext(dispatcher) {
            remote.fetchMenu()
        }

        // 3️⃣ Save to Room DB
        withContext(dispatcher) {
            val (menuEntity, categories, items) =
                remoteMenu.menuDocumentToEntities()

            local.insertFullMenu(
                menu = menuEntity,
                categories = categories,
                items = items
            )
        }
        // Save new version
        versionStore.saveLocalVersion(remoteMenu.menuVersion)

        emit(Resource.Success(remoteMenu.toDomain()))

    }.catch { e ->
        val localMenu = local.getMenuOnce()
        if (localMenu != null) {
            emit(Resource.Success(localMenu.toDomain()))
        } else {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}