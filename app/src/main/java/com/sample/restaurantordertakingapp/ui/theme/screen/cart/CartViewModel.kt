package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository
) : ViewModel() {

    /**
     * Single source of truth for Cart screen
     */
    val uiState: StateFlow<CartUiState> =
        cartRepo.observeCartItems()   // Flow<List<CartItem>>
            .map { items->

            // ---- Domain → UI mapping ----
                val uiItems = items.map { it.toUi() }

                // ---- Pricing calculation (business logic) ----
                val subtotal = items.sumOf { item ->
                    val unitPrice =
                        if (item.portion == PortionType.FULL)
                            item.fullPrice
                        else
                            item.halfPrice

                    unitPrice * item.quantity
                }

                val tax = subtotal * 0.05
                val total = subtotal + tax

                CartUiState(
                    items = uiItems,
                    subtotal = subtotal.toDouble(),
                    tax = tax,
                    total = total,
                    isEmpty = items.isEmpty(),
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CartUiState(isLoading = true)
            )

    /**
     * Derived state (no separate DB call needed)
     */
    val cartCount: StateFlow<Int> =
        uiState
            .map { it.items.sumOf { item -> item.quantity } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    /**
     * User actions
     */
    fun addToCart(item: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepo.addToCart(item)
        }
    }

    fun onQuantityChange(itemId: String, newQuantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepo.updateQuantity(itemId, newQuantity)
        }
    }

    fun onRemoveItem(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepo.removeItem(itemId)
        }
    }
}
