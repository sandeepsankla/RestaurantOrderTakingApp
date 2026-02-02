package com.sample.restaurantordertakingapp.data.remote.firebase

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderDto
import javax.inject.Inject

class FirebaseOrderListener @Inject constructor(
    private val firestore: FirebaseFirestore
) {

   fun listenForOrders(
        onNewOrder: (FirebaseOrderDto) -> Unit
    ) {
        firestore.collection("orders")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                snapshot.documentChanges.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        val order =
                            change.document.toObject(FirebaseOrderDto::class.java)
                        onNewOrder(order)
                    }
                }
            }
    }
}
