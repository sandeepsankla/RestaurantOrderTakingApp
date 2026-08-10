package com.sample.restaurantordertakingapp.domain.repo

import com.sample.restaurantordertakingapp.domain.model.CallSignal
import kotlinx.coroutines.flow.Flow

interface CallRepository {
    /** Reception: kitchen ko ping bhejo. */
    suspend fun sendCall(message: String)

    /** Kitchen: incoming pings sunо. */
    fun observeCalls(): Flow<CallSignal>
}
