package com.sample.restaurantordertakingapp.ui.theme.navigation

import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import android.media.RingtoneManager
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.call.CallKitchenDialog
import com.sample.restaurantordertakingapp.ui.theme.screen.call.CallViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.call.IncomingCallDialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import com.sample.restaurantordertakingapp.data.local.pref.AppMode
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.NotificationPermissionHandler
import com.sample.restaurantordertakingapp.ui.theme.screen.order.DailyReportScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.order.FulfillmentScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrdersScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.order.OrdersViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.role.ModeChooserDialog
import com.sample.restaurantordertakingapp.ui.theme.screen.role.PinDialog
import com.sample.restaurantordertakingapp.ui.theme.screen.role.RoleSetupScreen
import com.sample.restaurantordertakingapp.ui.theme.screen.role.RoleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val KEY_MENU_ITEM = "menuItem" // SavedStateHandle key

@Composable
fun AppNavigation() {
    val roleVm: RoleViewModel = hiltViewModel()

    var showPin by remember { mutableStateOf(false) }
    var showChooser by remember { mutableStateOf(false) }

    when (roleVm.mode) {
        null -> RoleSetupScreen(onDone = { m, pin -> roleVm.setup(m, pin) })
        AppMode.KITCHEN -> KitchenApp(onSwitchMode = { showPin = true })
        AppMode.RECEPTION -> ReceptionApp(onSwitchMode = { showPin = true })
    }

    if (showPin) {
        PinDialog(
            title = "Enter PIN to switch mode",
            onVerify = roleVm::checkPin,
            onSuccess = { showPin = false; showChooser = true },
            onDismiss = { showPin = false }
        )
    }
    if (showChooser) {
        ModeChooserDialog(
            onPick = { roleVm.switchTo(it); showChooser = false },
            onDismiss = { showChooser = false }
        )
    }
}

/** KITCHEN: sirf Orders. Koi menu/cart/drawer nahi. Lock icon se mode switch. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KitchenApp(onSwitchMode: () -> Unit) {
    val context = LocalContext.current
    val callVm: CallViewModel = hiltViewModel()
    var incoming by remember { mutableStateOf<String?>(null) }
    val ringtone = remember {
        RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
    }

    // Reception se call aaye to alarm + dialog
    LaunchedEffect(Unit) {
        var lastAt = System.currentTimeMillis()
        callVm.calls.collect { signal ->
            if (signal.at > lastAt) {
                lastAt = signal.at
                incoming = signal.message
                runCatching { ringtone.play() }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kitchen — Orders") },
                actions = {
                    IconButton(onClick = onSwitchMode) {
                        Icon(Icons.Default.Lock, contentDescription = "Switch mode")
                    }
                }
            )
        }
    ) { innerPadding ->
        val ordersVm: OrdersViewModel = hiltViewModel()
        val state by ordersVm.uiState.collectAsStateWithLifecycle()
        Box(Modifier.padding(innerPadding)) {
            OrdersScreen(state, ordersVm::refresh, ordersVm::onStationReady)
        }
    }

    incoming?.let { msg ->
        IncomingCallDialog(
            message = msg,
            onAck = {
                runCatching { ringtone.stop() }
                incoming = null
            }
        )
    }
}

/** RECEPTION: pura app (menu, cart, address, orders). */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ReceptionApp(onSwitchMode: () -> Unit) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val mainViewModel: MenuViewModel = hiltViewModel()
    // 🔔 ask permission ONCE when app starts
    NotificationPermissionHandler(
        onGranted = {
            mainViewModel.startListeningForOrders()
        }
    )

    val cartViewModel: CartViewModel = hiltViewModel()
    val cartCount by cartViewModel.cartCount.collectAsStateWithLifecycle()

    val callVm: CallViewModel = hiltViewModel()
    var showCall by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showHamburger = currentRoute == Screen.Menu.route
    val showCartIcon = currentRoute == Screen.Menu.route

    val showBackButton =
        currentRoute == Screen.Cart.route ||
                currentRoute == Screen.Address.route ||
                currentRoute == Screen.Orders.route ||
                currentRoute == Screen.Fulfillment.route ||
                currentRoute == Screen.Report.route

    val title = when (currentRoute) {
        Screen.Cart.route -> "Cart Items"
        Screen.Address.route -> "Takeaway Address"
        Screen.Orders.route -> "My Orders"
        Screen.Fulfillment.route -> "Serve & Payment"
        Screen.Report.route -> "Daily Report"
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
                onFulfillmentClick = {
                    scope.launch { drawerState.close() }
                    navController.navigateSingleTop(Screen.Fulfillment.route)
                },
                onReportClick = {
                    scope.launch { drawerState.close() }
                    navController.navigateSingleTop(Screen.Report.route)
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onSwitchMode()
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
                    onHamburgerClick = { scope.launch { drawerState.open() } },
                    onBack = { navController.popBackStack() },
                    onCartClick = { navController.navigateSingleTop(Screen.Cart.route) }
                )
            },
            floatingActionButton = {
                if (currentRoute == Screen.Menu.route) {
                    ExtendedFloatingActionButton(
                        onClick = { showCall = true },
                        icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                        text = { Text("Call Kitchen") }
                    )
                }
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
                    val orderPlaced by vm.orderPlaced.collectAsStateWithLifecycle()
                    val context = LocalContext.current

                    LaunchedEffect(orderPlaced) {
                        if (orderPlaced) {
                            Toast.makeText(context, "Order placed ✅", Toast.LENGTH_SHORT).show()
                            vm.onOrderHandled()
                            navController.navigate(Screen.Menu.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }

                    CartScreen(
                        state = state,
                        onQuantityChange = vm::onQuantityChange,
                        onRemoveItem = vm::onRemoveItem,
                        onProceed = { method -> vm.placeOrder(method) }
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
                            addToCart = { cartItem -> cartViewModel.addItem(cartItem) },
                            closeSheet = { navController.popBackStack() }
                        )
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Item not found")
                        }
                    }
                }

                composable(route = Screen.Orders.route) {
                    val ordersVm: OrdersViewModel = hiltViewModel()
                    val state by ordersVm.uiState.collectAsStateWithLifecycle()
                    OrdersScreen(state, ordersVm::refresh, ordersVm::onStationReady)
                }

                composable(route = Screen.Fulfillment.route) {
                    val ordersVm: OrdersViewModel = hiltViewModel()
                    val state by ordersVm.uiState.collectAsStateWithLifecycle()
                    FulfillmentScreen(
                        state,
                        ordersVm::refresh,
                        ordersVm::onAdvanceFulfillment,
                        ordersVm::onPaymentCollected
                    )
                }

                composable(route = Screen.Report.route) {
                    val ordersVm: OrdersViewModel = hiltViewModel()
                    val state by ordersVm.uiState.collectAsStateWithLifecycle()
                    DailyReportScreen(state)
                }
            }
        }
    }

    if (showCall) {
        CallKitchenDialog(
            onSend = { msg ->
                callVm.send(msg)
                showCall = false
                Toast.makeText(context, "Kitchen ko bheja: $msg", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showCall = false }
        )
    }
}
