package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import com.sample.restaurantordertakingapp.domain.model.PortionType

data class CartItemUi(
    val id: Int,
    val menuItemId: Int,
    val name: String,
    val imageUrl: String?,
    val quantity: Int,

    // 👇 prices for display
    val fullPrice: Int,
    val halfPrice: Int,

    // 👇 selection state
    val selectedPortion: PortionType,

    // 👇 display-only text
    val tableText: String
){
    val unitPrice: Int
        get() = if (selectedPortion == PortionType.FULL) fullPrice else halfPrice

    val totalPrice: Int
        get() = unitPrice * quantity

    fun getUnitPriceText(): String =
        "₹%,d".format(unitPrice)

    fun getTotalPriceText(): String =
        "₹%,d".format(totalPrice)
}