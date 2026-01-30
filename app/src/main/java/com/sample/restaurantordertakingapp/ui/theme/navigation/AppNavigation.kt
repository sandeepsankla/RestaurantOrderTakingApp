package com.sample.restaurantordertakingapp.ui.theme.navigation

import android.annotation.SuppressLint
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import com.sample.restaurantordertakingapp.ui.theme.component.common.AppBarWithCartBadge
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import kotlinx.coroutines.delay

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
    val cartCount by cartViewModel.cartCount.collectAsStateWithLifecycle() // Int expected

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route


    val showBackButton = currentRoute == "cart" || currentRoute == "address"
    val showCartIcon = currentRoute == "menu"

    val title = when (currentRoute) {
        "cart" -> "Cart Items"
        "address" -> "Takeaway Address"
        else -> "Tandoori Tadka House"
    }
    Scaffold(
        topBar = {
            AppBarWithCartBadge(
                appName = title,
                cartCount = cartCount,
                showCartIcon = showCartIcon,
                showBackButton = showBackButton,
                navController =  navController,
                onCartClick = {
                    if (backStackEntry?.destination?.route != Screen.Cart.route)
                    navController.navigateSingleTop(Screen.Cart.route)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Menu.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Menu.route) {

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
                    val vm: CartViewModel = hiltViewModel()
                    val state by vm.uiState.collectAsStateWithLifecycle()

                    CartScreen(
                        state = state,
                        onQuantityChange = vm::onQuantityChange,
                        onRemoveItem = vm::onRemoveItem,
                        onProceed = {
                            navController.navigate("address")
                        }
                    )
            }

            composable("address") {
                val viewModel: AddressViewModel = hiltViewModel()
                val state = viewModel.uiState
                LaunchedEffect(state.isSuccess) {
                    if (state.isSuccess) {
                        delay(1500)
                        navController.navigate(Screen.Menu.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }


                AddressScreen(
                    state = state,
                    onSocietyChange = viewModel::onSocietyChange,
                    onFlatNoChange = viewModel::onFlatNoChange,
                    onTowerChange = viewModel::onTowerChange,
                    onMobileChange = viewModel::onMobileChange,
                    onBack = { navController.popBackStack() },
                    onPlaceOrder = {
                        viewModel.onPlaceOrderClick()
                    }
                )
            }

            composable(Screen.Detail.route) {
                val menuItem = navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<MenuItemUi>(KEY_MENU_ITEM)


                if (menuItem != null) {
                    MenuItemDetailScreen1(
                        modifier = Modifier,
                        menuItem = menuItem,
                        addToCart = { cartItem ->
                            // call suspend through VM scope; no LaunchedEffect needed
                             cartViewModel.addItem(cartItem)
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
