package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.model.OrderStatus
import com.sample.restaurantordertakingapp.domain.repo.OrderRepository
import com.sample.restaurantordertakingapp.domain.usecase.order.ObserveOrdersUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.RefreshOrdersUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.UpdateOrderStatusUseCase
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toOrderUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    observeOrdersUseCase: ObserveOrdersUseCase,
    private val refreshOrdersUseCase: RefreshOrdersUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val orderRepository: OrderRepository
) : ViewModel() {


    val uiState: StateFlow<OrdersUiState> =
        observeOrdersUseCase()
            .map { domainOrders ->
                OrdersUiState(
                    isLoading = false,
                    orders = domainOrders.map { it.toOrderUi() },
                    error = null
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                OrdersUiState(isLoading = true)
            )

    fun refresh() {
        viewModelScope.launch {
            refreshOrdersUseCase()
        }
    }

    fun onOrderStatusClick(orderId: String, currentStatus: OrderStatus) {
        viewModelScope.launch {
            updateOrderStatusUseCase(orderId, currentStatus)
        }
    }

    /** Station (Tandoor/Kitchen) ne apna part ready mark kiya. */
    fun onStationReady(orderId: String, station: Station) {
        viewModelScope.launch {
            when (station) {
                Station.TANDOOR -> orderRepository.markTandoorReady(orderId)
                Station.KITCHEN -> orderRepository.markKitchenReady(orderId)
            }
        }
    }

    /** Reception: fulfillment agla step (serve/deliver). */
    fun onAdvanceFulfillment(orderId: String, currentStep: Int) {
        if (currentStep >= 2) return
        viewModelScope.launch {
            orderRepository.setFulfillmentStep(orderId, currentStep + 1)
        }
    }

    /** Reception: payment collect — method (Cash/UPI/Udhaar) save + step aage. */
    fun onPaymentCollected(orderId: String, method: String, currentStep: Int) {
        viewModelScope.launch {
            orderRepository.setPaymentMethod(orderId, method)
            if (currentStep < 2) orderRepository.setFulfillmentStep(orderId, currentStep + 1)
        }
    }
}

