package com.sample.restaurantordertakingapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String? = null,
    val imageUrl: String? = null
) : Parcelable {
    fun getFormattedPrice():String{
        return "₹$price"
    }
    fun getAmount(quantity : Int): Double{
        return price *  quantity
    }
}
