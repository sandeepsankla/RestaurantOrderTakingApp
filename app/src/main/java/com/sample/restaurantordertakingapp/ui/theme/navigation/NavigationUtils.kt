package com.sample.restaurantordertakingapp.ui.theme.navigation


import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController




// Title mapping helper
fun titleFor(dest: NavDestination?): String = when (dest?.route) {
    Screen.Menu.route -> Screen.Menu.title
    Screen.Cart.route -> Screen.Cart.title
    Screen.Detail.route -> Screen.Detail.title
    else -> "App"
}

// Navigation extension for single top navigation
fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) { saveState = true }
    }
}
