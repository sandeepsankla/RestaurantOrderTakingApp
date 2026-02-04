package com.sample.restaurantordertakingapp.domain.repo

interface OrderPullRepository {
    fun startListening()
    fun stopListening()
}