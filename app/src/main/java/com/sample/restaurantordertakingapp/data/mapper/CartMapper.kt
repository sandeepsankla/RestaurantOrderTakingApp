package com.sample.restaurantordertakingapp.data.mapper

import com.sample.restaurantordertakingapp.data.local.entity.CartItemEntity
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.PortionType

fun CartItemEntity.toDomain(): CartItem =
    CartItem(
        id = menuItemId,
        name = name,
        imageUrl = imageUrl,
        quantity = quantity,
        portion = if (isFull) PortionType.FULL else PortionType.HALF,
        fullPrice = fullPrice,
        halfPrice = halfPrice,
        tableId = tableId
    )

fun CartItem.toEntity(): CartItemEntity =
    CartItemEntity(
        menuItemId = id,
        name = name,
        imageUrl = imageUrl,
        quantity = quantity,
        isFull = portion == PortionType.FULL,
        fullPrice = fullPrice,
        halfPrice = halfPrice,
        tableId = tableId
    )
