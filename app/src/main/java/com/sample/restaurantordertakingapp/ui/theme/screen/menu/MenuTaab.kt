import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.data.model.Category
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.ui.theme.component.common.AppBarWithCartBadge
import com.sample.restaurantordertakingapp.ui.theme.component.common.BottomSheetWrapper
import com.sample.restaurantordertakingapp.ui.theme.component.menu.MenuItemCard
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuViewModel
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import kotlinx.coroutines.launch
import kotlin.collections.forEachIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen11(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    viewModel: MenuViewModel,
    onCartClick: () -> Unit,
    onMenuItemClick: (MenuItem) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    val scope = rememberCoroutineScope()

    // ✅ Material 3 bottom sheet state
    /*val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true  // disables half-expanded
    )*/

    // Example cart count (replace with your ViewModel flow)
    val cartCount by remember { mutableIntStateOf(5) }

    // ✅ Main Screen + BottomSheet combined
    /* Scaffold(
        topBar = {
            AppBarWithCartBadge(
                appName = "Tandoori Tadka House",
                cartCount = cartCount,
                onCartClick = onCartClick
            )
        }
    ) { innerPadding ->

        // Wrap your content in a Box — allows sheet to overlay properly
       Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {*/
            Column(modifier = Modifier.fillMaxSize()) {
                // ---------- Tabs ----------
                TabRow(selectedTabIndex = selectedTab) {
                    categories.forEachIndexed { i, cat ->
                        Tab(
                            selected = (i == selectedTab),
                            onClick = { selectedTab = i },
                            text = { Text(cat.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ---------- Grid ----------
                val menuItems =
                    if (categories.isNotEmpty()) categories[selectedTab].items
                    else emptyList()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(menuItems) { item ->
                        MenuItemCard(
                            menuItem = item,
                            onClick = {
                                selectedItem = item
                                onMenuItemClick(item)
                                //scope.launch { sheetState.show() }  // 👈 show the sheet
                            }
                        )
                    }
                }
            }

                BottomSheetWrapper(
                    showSheet = selectedItem != null,
                    onDismiss = { selectedItem = null }
                ) {
                    if (selectedItem != null) {
                        MenuItemDetailScreen1(
                            menuItem = selectedItem!!,
                            closeSheet = { selectedItem = null },
                            addToCart = { cartItem ->
                                scope.launch {
                                    viewModel.addToCart(cartItem)
                                    selectedItem = null
                                }
                            }
                        )
                    }
                }
            //    }-- Bottom Sheet (Material 3) ----------
            /*    if (selectedItem != null) {
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch {
                            sheetState.hide()
                            selectedItem = null
                        }
                    },
                    sheetState = sheetState,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // pass close/add lambdas that hide the sheet and clear selected item
                    MenuItemDetailScreen1(
                        modifier = Modifier.fillMaxWidth(),
                        menuItem = selectedItem!!,
                        closeSheet = {
                            scope.launch {
                                sheetState.hide()
                                selectedItem = null
                            }
                        },
                        addToCart = { cartItem ->
                            scope.launch {
                                viewModel.addToCart(cartItem)
                                sheetState.hide()
                                selectedItem = null
                            }
                        }
                    )
                }
            }*/
        }
   /* }
}*/
