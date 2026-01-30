package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.usecase.order.GetOrdersUseCase
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toOrderUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    var uiState by mutableStateOf(OrdersUiState())
        private set

    init {
        loadOrders()
    }

  /*  private fun loadOrders() {
        viewModelScope.launch {
            uiState = getOrdersUseCase()
                .map { it.toOrderUi() }   // ⭐ Order → OrderUi
        }
    }*/
    fun loadOrders() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            try {
                val orders = getOrdersUseCase()
                    .map { it.toOrderUi() }

                uiState = uiState.copy(
                    isLoading = false,
                    orders = orders
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Failed to load orders"
                )
            }
        }
    }
}
