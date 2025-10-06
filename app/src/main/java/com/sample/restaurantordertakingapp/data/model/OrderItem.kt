package com.sample.restaurantordertakingapp.data.model

data class OrderItem(
    val id: String,
    val quantity: Int,
    val isFull: Boolean = true,
    val price: Double,
    val table: String?,
    val takeAway: Boolean = false
){
   fun getAmount(): Double{
        return price * quantity
    }
}


