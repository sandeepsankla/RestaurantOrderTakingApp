package com.sample.restaurantordertakingapp.ui.theme.component.common

//import androidx.compose.material.icons.Icon
import android.R.attr.navigationIcon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithCartBadge(
    appName: String = "Tandoori Tadka House",
    cartCount: Int, // pass dynamic cart item count from ViewModel
    onCartClick: () -> Unit,
    showCartIcon: Boolean,
    showBackButton: Boolean,
    navController: NavHostController
) {
    TopAppBar(
        title = {
            Text(
                text = appName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },

            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
        actions = {
            if(showCartIcon){
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text(cartCount.toString())
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                }
            }
            // Badge Box for Cart Icon

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
