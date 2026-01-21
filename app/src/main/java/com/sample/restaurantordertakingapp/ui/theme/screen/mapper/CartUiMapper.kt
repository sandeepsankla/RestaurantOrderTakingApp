package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartItemUi

fun CartItem.toUi(): CartItemUi =
    CartItemUi(
        id = id,
        name = name,
        imageUrl = imageUrl,
        quantity = quantity,
        fullPriceText = "₹$120",
        halfPriceText = "₹$halfPrice",
        selectedPortion = PortionType.FULL,
        tableText = tableId ?: "Takeaway"
    )