package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.sample.restaurantordertakingapp.domain.model.CartState
import com.sample.restaurantordertakingapp.ui.theme.component.cart.CartItemRow
import com.sample.restaurantordertakingapp.ui.theme.component.cart.PricingSummarySection
import com.sample.restaurantordertakingapp.ui.theme.component.cart.TakeawayAddressSection
import com.sample.restaurantordertakingapp.utils.createSampleCartState
import com.sample.restaurantordertakingapp.utils.removeItemFromCart
import com.sample.restaurantordertakingapp.utils.updateCartQuantity


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
) {
    var cartState by remember { mutableStateOf<CartState>(createSampleCartState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Text(
            text = "Cart Items",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Cart Items List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(cartState.items) { item ->
                CartItemRow(
                    item = item,
                    onQuantityChange = { newQuantity ->
                        cartState = updateCartQuantity(cartState, item.id, newQuantity)
                    },
                    onRemove = {
                        cartState = removeItemFromCart(cartState, item.id)
                    }
                )
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }

        // Pricing Summary
        PricingSummarySection(
            cartState = cartState//,
           // onProceedToCheckout =
        )

        // Takeaway Address Section
        TakeawayAddressSection()
    }
}

