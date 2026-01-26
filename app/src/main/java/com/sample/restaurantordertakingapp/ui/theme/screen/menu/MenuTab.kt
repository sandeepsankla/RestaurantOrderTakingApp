import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.sample.restaurantordertakingapp.ui.theme.component.common.BottomSheetWrapper
import com.sample.restaurantordertakingapp.ui.theme.component.menu.MenuItemCard
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartItemUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.CategoryUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu_details.MenuItemDetailScreen1
import kotlin.collections.forEachIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen(
    categories: List<CategoryUi>,
    onItemClick: (MenuItemUi) -> Unit,
    onAddToCart: (CartItemUi) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedItem by remember { mutableStateOf<MenuItemUi?>(null) }

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
                addToCart = { cartItem ->
                    onAddToCart(cartItem)   // 👈 SINGLE CALL
                    selectedItem = null
                }
            )
        }
    }
}




