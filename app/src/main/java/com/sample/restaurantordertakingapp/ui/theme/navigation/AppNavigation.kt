package com.sample.restaurantordertakingapp.ui.theme.navigation

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
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.NotificationPermissionHandler
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrdersScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrdersViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val KEY_MENU_ITEM = "menuItem" // SavedStateHandle key

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val mainViewModel: MenuViewModel = hiltViewModel()
    // 🔔 ask permission ONCE when app starts
    NotificationPermissionHandler {
        mainViewModel.startListeningForOrders()
    }

    val cartViewModel: CartViewModel = hiltViewModel()
    val cartCount by cartViewModel.cartCount.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showHamburger = currentRoute == Screen.Menu.route
    val showCartIcon = currentRoute == Screen.Menu.route

    val showBackButton =
        currentRoute == Screen.Cart.route ||
                currentRoute == Screen.Address.route ||
                currentRoute == Screen.Orders.route

    val title = when (currentRoute) {
        Screen.Cart.route -> "Cart Items"
        Screen.Address.route -> "Takeaway Address"
        Screen.Orders.route -> "My Orders"
        else -> "Tandoori Tadka House"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showHamburger,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onOrdersClick = {
                    scope.launch { drawerState.close() }
                    navController.navigateSingleTop(Screen.Orders.route)
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }

                    // TODO: clear session / token
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = title,
                    showHamburger = showHamburger,
                    showBackButton = showBackButton,
                    showCartIcon = showCartIcon,
                    cartCount = cartCount,
                    onHamburgerClick = {
                        scope.launch { drawerState.open() }
                    },
                    onBack = { navController.popBackStack() },
                    onCartClick = {
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

                composable(Screen.Address.route) {
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
                            // 🔥 THIS LINE FIXES YOUR BUG
                            viewModel.onNavigationHandled()
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

                composable (route = Screen.Orders.route ){
                  val  ordersVm: OrdersViewModel = hiltViewModel()
                  val state by ordersVm.uiState.collectAsStateWithLifecycle()
                    OrdersScreen(state, ordersVm::refresh, ordersVm::onOrderStatusClick)
                }
            }
        }
    }
}
