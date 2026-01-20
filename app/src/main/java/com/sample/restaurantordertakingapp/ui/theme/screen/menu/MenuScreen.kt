package com.sample.restaurantordertakingapp.ui.theme.screen.menu

import MenuTabScreen11
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.restaurantordertakingapp.data.model.Category
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.network.Resource
import com.sample.restaurantordertakingapp.ui.theme.component.common.AppBarWithCartBadge
import com.sample.restaurantordertakingapp.ui.theme.component.menu.MenuItemCard
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MenuScreen(  onCartClick: () -> Unit,
                onItemClick: (MenuItem) -> Unit ) {
    val viewModel: MenuViewModel = hiltViewModel()
    LaunchedEffect(Unit){
        viewModel.loadMenu()
    }
    val menuState by viewModel.menuState.collectAsState()
    when (menuState) {
        is Resource.Loading -> {
            // show skeleton / loading indicator
            //MenuSkeleton()
        }
        is Resource.Error -> {
            Text("Error: ${(menuState as Resource.Error).message}")
        }
        is Resource.Success -> {
            val items = (menuState as Resource.Success).data
            Log.d("sasa","$items")
            MenuTabScreen11(modifier = Modifier.padding(12.dp),
                categories = items.categories,
                viewModel,
                onCartClick =  onCartClick,
                onMenuItemClick = {
                    onItemClick(it)
                }
            )
        }
    }
}


/*@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen(modifier: Modifier, categories: List<Category>, viewModel: MenuViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSheet by remember { mutableStateOf(false) }
    var cartCount by remember { mutableIntStateOf(5) } // todo sandeep replace with your ViewModel state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true  // optionally avoid half expanded state
    )
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppBarWithCartBadge(
                appName = "Tandoori Tadka House",
                cartCount = cartCount,
                onCartClick =  { }

            )
        }
    ){innerPadding ->

            Column(modifier = modifier.fillMaxSize().padding( innerPadding)) {
                TabRow(selectedTabIndex = selectedTab) {
                    categories.forEachIndexed { i, cat ->
                        Tab(
                            selected = (i == selectedTab),
                            onClick = { selectedTab = i },
                            text = { Text(cat.name) }
                        )
                    }
                }

                // The grid of items for the selected category
                val menuItems =
                    if (categories.isNotEmpty()) categories[selectedTab].items
                    else emptyList()
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(menuItems.size) { index ->
                        MenuItemCard(
                            menuItem = menuItems[index],
                            onClick = {
                                showSheet = !showSheet
                                selectedItem = menuItems[index]
                                scope.launch {
                                    sheetState.show()

                                }
                            }

                        )
                    }


                }
                if (showSheet && selectedItem != null) {
                    MenuItemDetailScreen1(
                        modifier = Modifier.fillMaxHeight(),
                        menuItem = selectedItem!!,
                        closeSheet = {
                            showSheet = !showSheet
                            scope.launch {
                                sheetState.hide()
                            }
                        },
                        addToCart = { cartItem ->
                            scope.launch {
                                viewModel.addToCart(cartItem)
                                delay(1000)
                                showSheet = !showSheet
                                sheetState.hide()
                               // Toast.makeText(, "Item added to cart", Toast.LENGTH_SHORT).show()
                            }
                            //
                        })
                }
            }
        }
}*/


