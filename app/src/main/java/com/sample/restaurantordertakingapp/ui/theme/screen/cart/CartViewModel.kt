package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.domain.usecase.cart.AddToCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.CalculateCartSummaryUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.ObserveCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.RemoveItemUseCase
import com.sample.restaurantordertakingapp.domain.usecase.cart.UpdateQuantityUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.PlaceOrderUseCase
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toDomain
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val observeCart: ObserveCartUseCase,
    private val calculateSummary: CalculateCartSummaryUseCase,
    private val addToCart: AddToCartUseCase,
    private val updateQuantity: UpdateQuantityUseCase,
    private val removeItem: RemoveItemUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase
) : ViewModel() {

    // Cart se seedha order place — success par UI navigate karega
    private val _orderPlaced = MutableStateFlow(false)
    val orderPlaced: StateFlow<Boolean> = _orderPlaced

    fun placeOrder(paymentMethod: String? = null) {
        viewModelScope.launch {
            runCatching {
                placeOrderUseCase(
                    address = Address(society = null, flatNo = null, tower = null, mobile = "", orderId = ""),
                    paymentMethod = paymentMethod
                )
            }.onSuccess { _orderPlaced.value = true }
        }
    }

    fun onOrderHandled() {
        _orderPlaced.value = false
    }


    val uiState: StateFlow<CartUiState> = observeCart()
            .map { items ->

                val summary = calculateSummary(items)

                CartUiState(
                    items = items.map { it.toUi() },
                    subtotal = summary.subtotal,
                    tax = summary.tax,
                    total = summary.total,
                    isEmpty = summary.isEmpty,
                    isLoading = false
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                CartUiState(isLoading = true)
            )

    val cartCount: StateFlow<Int> =
        uiState
            .map { it.items.sumOf { item -> item.quantity } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                0
            )

    fun addItem(item: CartItemUi) =
        viewModelScope.launch {
            addToCart(item.toDomain())
        }

    fun onQuantityChange(itemId: Int, qty: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            updateQuantity(itemId, qty)
        }

    fun onRemoveItem(itemId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            removeItem(itemId)
        }
}