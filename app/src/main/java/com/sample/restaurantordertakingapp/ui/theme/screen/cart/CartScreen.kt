package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import com.sample.restaurantordertakingapp.ui.theme.component.cart.CartItemRow
import com.sample.restaurantordertakingapp.ui.theme.component.cart.EmptyCartView
import com.sample.restaurantordertakingapp.ui.theme.component.cart.PricingSummarySection
import com.sample.restaurantordertakingapp.ui.theme.component.common.LoadingView


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartUiState,
    onQuantityChange: (Int, Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onProceed: () -> Unit
) {
    Scaffold(
        bottomBar = {
            if (!state.isEmpty && !state.isLoading) {
                CartBottomBar(
                    total = state.total,
                    onProceed = onProceed,
                    modifier = Modifier
                )
            }
        }
    ) { padding ->

        when {
            state.isLoading -> LoadingView(padding)
            state.isEmpty -> EmptyCartView(padding)

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 120.dp // 👈 space for bottom bar
                    )
                ) {

                    items(state.items, key = { it.id }) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = {
                                onQuantityChange(item.id, it)
                            },
                            onRemove = {
                                onRemoveItem(item.id)
                            }
                        )
                        HorizontalDivider()
                    }

                    item {
                        PricingSummarySection(
                            subtotal = state.subtotal,
                            tax = state.tax,
                            total = state.total
                        )
                    }
                }
            }
        }
    }
}




/*
fun CartScreen(
    state: CartUiState,
    onQuantityChange: (Int, Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onProceed: () -> Unit
) {
    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Cart Items") })
//        },
        bottomBar = {
            CartBottomBar(
                total = state.total,
                onProceed = onProceed
            )
        }
    ) { padding ->

        when {
            state.isLoading -> LoadingView(padding)
            state.isEmpty ->   EmptyCartView(padding)

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 120.dp
                    )
                ) {
                    items(
                        items = state.items,
                        key = { it.id }
                    ) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = {
                                onQuantityChange(item.id, it)
                            },
                            onRemove = {
                                onRemoveItem(item.id)
                            }
                        )
                        HorizontalDivider()
                    }

                    item {
                        PricingSummarySection(
                            subtotal = state.subtotal,
                            tax = state.tax,
                            total = state.total
                        )
                    }
                }
            }
        }
    }
}


*/
