package com.sample.restaurantordertakingapp.ui.theme.navigation

import android.annotation.SuppressLint
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import kotlinx.coroutines.launch

//@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import com.sample.restaurantordertakingapp.ui.theme.component.common.AppBarWithCartBadge

private const val KEY_MENU_ITEM = "menuItem" // SavedStateHandle key

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // shared VM (graph root scope)
    val cartViewModel: CartViewModel = hiltViewModel()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentTitle = remember(backStackEntry?.destination) {
        titleFor(backStackEntry?.destination)
    }

    // expose count; adapt type if your VM emits List -> size
    val cartCount by cartViewModel.cartItems.collectAsStateWithLifecycle() // Int expected

    Scaffold(
        topBar = {
            AppBarWithCartBadge(
                appName = "Tandoori Tadka House",
                cartCount = cartCount,
                onCartClick = {
                    if (backStackEntry?.destination?.route != Screen.Cart.route)
                    navController.navigateSingleTop(Screen.Cart.route)
                }
            )

           /* CenterAlignedTopAppBar(
                //title = { Text(currentTitle) },
                title = { Text("hello i am sandy") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) Badge { Text(cartCount.toString()) }
                        }
                    ) {
                        IconButton(onClick = {
                            if (backStackEntry?.destination?.route != Screen.Cart.route) {
                                navController.navigateSingleTop(Screen.Cart.route)
                            }
                        }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )*/
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Menu.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Menu.route) {
                val scope = rememberCoroutineScope()

                MenuScreen(
                    onCartClick = { navController.navigateSingleTop(Screen.Cart.route) },
                    onItemClick = { menuItem ->
                        // pass whole object (Parcelable) via SavedStateHandle
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(KEY_MENU_ITEM, menuItem)

                        navController.navigate(Screen.Detail.route)
                    }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen()
            }

            composable(Screen.Detail.route) {
                val menuItem = navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<MenuItem>(KEY_MENU_ITEM)

                val scope = rememberCoroutineScope()

                if (menuItem != null) {
                    MenuItemDetailScreen1(
                        modifier = Modifier,
                        menuItem = menuItem,
                        addToCart = { cartItem ->
                            // call suspend through VM scope; no LaunchedEffect needed
                            scope.launch { cartViewModel.addToCart(cartItem) }
                        },
                        closeSheet = { navController.popBackStack() }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Item not found")
                    }
                }
            }
        }
    }
}
