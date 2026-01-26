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
        fullPrice = fullPrice,
        halfPrice = halfPrice,
        selectedPortion = PortionType.FULL,
        tableText = tableId ?: "Takeaway"
    )
fun CartItemUi.toDomain(): CartItem =
    CartItem(  id = id,
        name = name,
        imageUrl = imageUrl,
        quantity = quantity,
        fullPrice = fullPrice,
        halfPrice = halfPrice,
        portion = selectedPortion,
        tableId = tableText
    )

