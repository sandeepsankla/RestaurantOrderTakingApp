import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.data.model.Category
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.ui.theme.component.common.BottomSheetWrapper
import com.sample.restaurantordertakingapp.ui.theme.component.menu.MenuItemCard
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import kotlin.collections.forEachIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen(
    categories: List<Category>,
    onItemClick: (MenuItem) -> Unit,
    onAddToCart: (CartItem) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            categories.forEachIndexed { i, cat ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = { Text(cat.name) }
                )
            }
        }

        val menuItems = categories[selectedTab].items

        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(menuItems) { item ->
                MenuItemCard(item) {
                    selectedItem = item
                }
            }
        }
    }

    BottomSheetWrapper(
        showSheet = selectedItem != null,
        onDismiss = { selectedItem = null }
    ) {
        selectedItem?.let { item ->
            MenuItemDetailScreen1(
                menuItem = item,
                closeSheet = { selectedItem = null },
                addToCart = {
                    onAddToCart(it)
                    selectedItem = null
                }
            )
        }
    }
}



/*
fun MenuTabScreen(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    viewModel: MenuViewModel,
    onCartClick: () -> Unit,
    onMenuItemClick: (MenuItem) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    //val scope = rememberCoroutineScope()
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
                    viewModel.addToCart(cartItem)
                    selectedItem = null
                }
            )
        }
    }
    //    }-- Bottom Sheet (Material 3) ----------
    */
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
            }*//*

}
   */


