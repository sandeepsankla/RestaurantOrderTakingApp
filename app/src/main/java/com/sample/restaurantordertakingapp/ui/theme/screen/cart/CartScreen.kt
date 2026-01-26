package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.sample.restaurantordertakingapp.ui.theme.component.cart.CartItemRow
import com.sample.restaurantordertakingapp.ui.theme.component.cart.PricingSummarySection
import com.sample.restaurantordertakingapp.ui.theme.component.cart.TakeawayAddressSection


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartUiState,
    onQuantityChange: (itemId: Int, newQuantity: Int) -> Unit,
    onRemoveItem: (itemId: Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart Items") }
            )
        }
    ) { padding ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.isEmpty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.White)
                ) {

                    // ---- Cart items list ----
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(
                            items = state.items,
                            key = { it.id }
                        ) { item ->

                            CartItemRow(
                                item = item,
                                onQuantityChange = { qty ->
                                    onQuantityChange(item.id, qty)
                                },
                                onRemove = {
                                    onRemoveItem(item.id)
                                }
                            )

                            HorizontalDivider(thickness = 0.5.dp, color = DividerDefaults.color)
                        }
                    }

                    // ---- Pricing summary ----
                    PricingSummarySection(
                        subtotal = state.subtotal,
                        tax = state.tax,
                        total = state.total
                    )

                    // ---- Takeaway address ----
                    TakeawayAddressSection()
                }
            }
        }
    }
}

