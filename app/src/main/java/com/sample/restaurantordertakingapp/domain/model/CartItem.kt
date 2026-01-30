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
    val society: String?,
    val tower: String?,
    val flat: String?,
    val mobileNo: Int
)

data class PriceDetails(
    val subTotal : Double,
    val gst : Double,
    val total : Double,
    val discount : Double
)





