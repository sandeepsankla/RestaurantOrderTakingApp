package com.sample.restaurantordertakingapp.ui.theme.screen.menu

import MenuTabScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.restaurantordertakingapp.network.Resource
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toDomain


@Composable
fun MenuScreen(
    onCartClick: () -> Unit,
    onItemClick: (MenuItemUi) -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val menuState by viewModel.menuState.collectAsStateWithLifecycle()

    when (val state = menuState) {
        is Resource.Loading -> {
            // MenuSkeleton()
        }

        is Resource.Error -> {
           /* MenuError(
                message = (menuState as Resource.Error).message,
                onRetry = { viewModel.loadMenu() }
            )*/

            Text("Error: ${state.message}")
        }

        is Resource.Success -> {
            MenuTabScreen(
                categories = state.data.categories,
                onItemClick = onItemClick,
                onAddToCart = { uiItem ->
                    viewModel.addToCart(uiItem.toDomain())
                }
            )
        }
    }
}


