package com.sample.restaurantordertakingapp.ui.theme.screen.cart

data class CartUiState(
    val items: List<CartItemUi> = emptyList(),

    // pricing
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,

    // screen state
    val isLoading: Boolean = false,
    val isEmpty: Boolean = true
)
