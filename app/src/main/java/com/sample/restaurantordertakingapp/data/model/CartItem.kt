package com.sample.restaurantordertakingapp.data.model

import com.sample.restaurantordertakingapp.data.local.CartItemEntity


data class CartItem(
    val id: String,
    val imageUrl : String?,
    val quantity: Int,
    val isFull: Boolean = true,
    val price: Double,
    val table: String?,
    val takeAway: Boolean = false,
    val name :String
){

}

data class CartState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0
)

data class CartDetails(
    val cartItems: List<CartItem>,
    val address: Address,
    val priceDetails: PriceDetails
)

data class Address(
    val society: String,
    val tower : String,
    val flat : String,
    val mobileNo : Int
)

data class PriceDetails(
    val subTotal : Double,
    val gst : Double,
    val total : Double,
    val discount : Double
)





