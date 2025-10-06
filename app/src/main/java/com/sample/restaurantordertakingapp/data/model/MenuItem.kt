package com.sample.restaurantordertakingapp.data.model

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String? = null,
    val imageUrl: String? = null
){
    fun getFormattedPrice():String{
        return "₹$price"
    }
    fun getAmount(quantity : Int): Double{
        return price *  quantity
    }
}
