package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.data.local.enttity.CartItemEntity
import com.sample.restaurantordertakingapp.domain.model.CartItem




    fun CartItemEntity.toCartItem(): CartItem {
        return CartItem(
            id = this.menuItemId,
            imageUrl = this.imageUrl,
            name = this.name,
            price = this.price.toDouble(),
            quantity = this.quantity,
            isFull = this.isFull,
            table = this.table,
            takeAway = this.takeAway,
            halfPrice = this.hashCode()
        )
    }

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        menuItemId = this.id,
        name = this.name,
        price = this.price.toInt(),
        quantity = this.quantity,
        imageUrl = this.imageUrl,
        isFull = this.isFull,
        table = this.table,
        takeAway = this.takeAway
    )
}