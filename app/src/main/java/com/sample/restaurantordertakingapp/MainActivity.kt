package com.sample.restaurantordertakingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuScreen
import com.sample.restaurantordertakingapp.ui.theme.theme.RestaurantOrderTakingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantOrderTakingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                   //Greeting("sandeep", modifier = Modifier.padding(it))
                   // MenuTabScreen(modifier = Modifier.padding(it))
                    MenuScreen(context = this.baseContext)
                }
            }
        }
    }
}
/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen(modifier: Modifier) {
//fun MenuTabScreen(modifier: Modifier, categories : List<Category>, onMenuItemClick : (MenuItem) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
//            categories.forEachIndexed { i, cat ->
//                Tab(selected = i == selectedTab, onClick = { selectedTab = i }) {
//                    Text(cat.name, modifier = Modifier.padding(16.dp))
//                }
//            }
        }

        // Placeholder grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {

//            items(count = categories.size) {
//                categories.forEach { category ->
//                    MenuItemCardSkeleton(category.items)
//                }
//
//            }
        }
    }
}

@Composable
fun MenuItemCardSkeleton(menuItems: MenuItem) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.2f))
            )
            Text(menuItems.name)
        }

}

*/


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Determine title dynamically
    val currentTitle = when {
        currentRoute == Screen.Menu.route -> Screen.Menu.title
        currentRoute == Screen.Cart.route -> Screen.Cart.title
        currentRoute?.startsWith("detail/") == true -> Screen.Detail.title
        else -> "App"
    }

    // Get cart count from ViewModel
    val cartViewModel: CartViewModel = hiltViewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTitle) },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge { Text(cartItems.size.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = {
                            // navigate to cart only if not already there
                            if (currentRoute != Screen.Cart.route) {
                                navController.navigate(Screen.Cart.route)
                            }
                        }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
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
                    onItemClick = { menuItem ->
                        navController.navigate("detail/${menuItem.id}")
                    }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { backEntry ->
                val itemId = backEntry.arguments?.getString("itemId") ?: ""
                DetailScreen(
                    itemId = itemId,
                    onAddToCart = { menuItem, qty, isFull ->
                        cartViewModel.addToCart(menuItem, qty, isFull)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}*/
