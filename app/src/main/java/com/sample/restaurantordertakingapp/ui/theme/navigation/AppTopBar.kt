package com.sample.restaurantordertakingapp.ui.theme.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    showHamburger: Boolean,
    showBackButton: Boolean,
    showCartIcon: Boolean,
    cartCount: Int,
    onHamburgerClick: () -> Unit,
    onBack: () -> Unit,
    onCartClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            when {
                showHamburger -> {
                    IconButton(onClick = onHamburgerClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
                showBackButton -> {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        },
        actions = {
            if (showCartIcon) {
                CartIconWithBadge(
                    count = cartCount,
                    onClick = onCartClick
                )
            }
        }
    )
}

@Composable
fun CartIconWithBadge(
    count: Int,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart"
            )
        }

        if (count > 0) {
            Box(
                modifier = Modifier
                    .offset(x = (-4).dp, y = 4.dp)
                    .size(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

