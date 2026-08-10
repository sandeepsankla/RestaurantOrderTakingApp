package com.sample.restaurantordertakingapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sample.restaurantordertakingapp.domain.model.CallSignal
import com.sample.restaurantordertakingapp.domain.repo.CallRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CallRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CallRepository {

    private val doc get() = firestore.collection("calls").document("current")

    override suspend fun sendCall(message: String) {
        doc.set(
            mapOf(
                "message" to message,
                "at" to System.currentTimeMillis()
            )
        ).await()
    }

    override fun observeCalls(): Flow<CallSignal> = callbackFlow {
        val registration = doc.addSnapshotListener { snapshot, _ ->
            val message = snapshot?.getString("message")
            val at = snapshot?.getLong("at")
            if (message != null && at != null) {
                trySend(CallSignal(message, at))
            }
        }
        awaitClose { registration.remove() }
    }
}
