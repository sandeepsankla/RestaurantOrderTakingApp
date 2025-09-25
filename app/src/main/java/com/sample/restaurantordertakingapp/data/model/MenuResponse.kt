package com.sample.restaurantordertakingapp.data.model


data class MenuResponse(
    val menu: Menu
)

data class Menu(
    val categories: List<Category>
)

data class Category(
    val id: String,
    val name: String,
    val items: List<MenuItem>
)