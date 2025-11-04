package com.sample.restaurantordertakingapp.ui.theme.screen

sealed class Screen(val route: String, val title: String) {
    object Menu : Screen("menu", "Menu")
    object Cart : Screen("cart", "Cart")
    object Detail : Screen("detail/{itemId}", "Details")
}
