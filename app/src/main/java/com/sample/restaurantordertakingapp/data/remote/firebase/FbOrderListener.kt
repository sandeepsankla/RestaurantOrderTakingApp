package com.sample.restaurantordertakingapp.data.remote.firebase

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.sample.restaurantordertakingapp.data.remote.firebase.model.FirebaseOrderDto
import javax.inject.Inject

class FirebaseOrderListener @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun listen(
        onOrderReceived: (FirebaseOrderDto) -> Unit
    ): ListenerRegistration {

        return firestore
            .collection("orders")
            .addSnapshotListener { snapshots, error ->

                if (error != null || snapshots == null) return@addSnapshotListener

                snapshots.documentChanges.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        val order =
                            change.document.toObject(FirebaseOrderDto::class.java)

                        onOrderReceived(order)
                    }
                }
            }
    }
}
