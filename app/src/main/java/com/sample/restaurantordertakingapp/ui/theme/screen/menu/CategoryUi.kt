package com.sample.restaurantordertakingapp.ui.theme.screen.menu


data class CategoryUi(
    val id: Int ,
    val name: String = "",
    val items: List<MenuItemUi> =emptyList<MenuItemUi>()
)