package com.sample.restaurantordertakingapp.ui.theme.screen.order

data class OrdersUiState(
    val isLoading: Boolean = false,
    val orders: List<OrderUi> = emptyList(),
    val error: String? = null
)
