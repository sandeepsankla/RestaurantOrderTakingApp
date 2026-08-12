import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.ui.theme.component.common.BottomSheetWrapper
import com.sample.restaurantordertakingapp.ui.theme.component.menu.MenuItemCard
import com.sample.restaurantordertakingapp.ui.theme.component.voice.VoiceOrderDialog
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
    var showVoiceDialog by remember { mutableStateOf(false) }

    val allMenuItems = remember(categories) {
        categories.flatMap { it.items }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 8.dp
            ) {
                categories.forEachIndexed { i, cat ->
                    Tab(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        text = { Text(cat.name, maxLines = 1) }
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

        // 🎙️ Voice Order Floating Action Button
        ExtendedFloatingActionButton(
            onClick = { showVoiceDialog = true },
            icon = { Icon(Icons.Default.Mic, contentDescription = "Voice Order") },
            text = { Text("Voice Order") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
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

    // 🎙️ Voice Order Dialog
    if (showVoiceDialog) {
        VoiceOrderDialog(
            showDialog = showVoiceDialog,
            allItems = allMenuItems,
            onDismiss = { showVoiceDialog = false },
            onConfirmAddItems = { parsedItems ->
                parsedItems.forEach { parsed ->
                    onAddToCart(parsed.toCartItemUi())
                }
            }
        )
    }
}





