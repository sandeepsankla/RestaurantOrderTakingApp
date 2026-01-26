package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.data.local.entity.CartItemEntity
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.PortionType


fun CartItemEntity.toCartItem(): CartItem {
        return CartItem(
            id = this.menuItemId,
            imageUrl = this.imageUrl,
            name = this.name,
            halfPrice = this.halfPrice,
            fullPrice = this.fullPrice,
            quantity = this.quantity,
            tableId = this.tableId,
            portion = if (isFull) PortionType.FULL else PortionType.HALF
        )
    }

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        menuItemId = this.id,
        name = this.name,
        halfPrice = this.halfPrice,
        fullPrice = this.fullPrice,
        quantity = this.quantity,
        imageUrl = this.imageUrl,
        isFull = if(portion  == PortionType.FULL) true else false,
        tableId = this.tableId
    )
}