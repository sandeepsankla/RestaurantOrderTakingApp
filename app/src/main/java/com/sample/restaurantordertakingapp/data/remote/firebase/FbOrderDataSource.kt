package com.sample.restaurantordertakingapp.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseOrderDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun pushOrder(order: FirebaseOrderDto) {
        firestore
            .collection("orders")
            .document(order.orderId)
            .set(order)
            .await()
    }
}
