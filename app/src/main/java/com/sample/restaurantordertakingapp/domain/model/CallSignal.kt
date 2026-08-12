package com.sample.restaurantordertakingapp.domain.model

/** Reception -> Kitchen attention ping. */
data class CallSignal(
    val message: String,
    val at: Long
)
