package com.sample.restaurantordertakingapp.ui.theme.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class Screen(val route: String, val title: String) {
    data object Menu : Screen("menu", "Menu")
    data object Cart : Screen("cart", "Cart")
    data object Detail : Screen("detail", "Item detail")

    // Helper function to create the route with argument
    fun createDetailRoute(itemId: String) = "detail/$itemId"
}

