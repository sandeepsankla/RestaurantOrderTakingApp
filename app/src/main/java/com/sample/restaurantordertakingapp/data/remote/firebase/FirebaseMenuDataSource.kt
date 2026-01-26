package com.sample.restaurantordertakingapp.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sample.restaurantordertakingapp.data.remote.firebase.model.MenuDocument
import com.sample.restaurantordertakingapp.data.remote.firebase.model.MenuResponseDocument
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMenuDataSource  @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun fetchMenu(): MenuDocument {
        val snapshot = firestore
            .collection("menus")
            .document("default")
            .get()
            .await()

        return snapshot.toObject(MenuDocument::class.java)
            ?: throw IllegalStateException("Menu not found")
    }
}