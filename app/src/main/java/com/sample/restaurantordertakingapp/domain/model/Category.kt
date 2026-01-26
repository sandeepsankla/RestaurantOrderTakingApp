package com.sample.restaurantordertakingapp.domain.model


data class Category(
    val id: Int,
    val menuId: Int,
    val name: String,
    val items: List<MenuItem>
)