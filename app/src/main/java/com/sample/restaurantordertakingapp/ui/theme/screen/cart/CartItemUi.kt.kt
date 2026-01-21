package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import com.sample.restaurantordertakingapp.domain.model.PortionType

data class CartItemUi(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val quantity: Int,

    // 👇 prices for display
    val fullPriceText: String,
    val halfPriceText: String,

    // 👇 selection state
    val selectedPortion: PortionType,

    // 👇 display-only text
    val tableText: String
)