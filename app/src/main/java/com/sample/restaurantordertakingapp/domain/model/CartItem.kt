package com.sample.restaurantordertakingapp.domain.model


data class CartItem(
    val id: Int,
    val menuItemId: Int,
    val imageUrl : String?,
    val quantity: Int,
    val portion: PortionType,
    val halfPrice: Int,
    val fullPrice: Int,
    val tableId: String?,
    val name :String,
){
    val unitPrice: Int
        get() = if (portion == PortionType.FULL) fullPrice else halfPrice

    val totalPrice: Int
        get() = unitPrice * quantity

}




