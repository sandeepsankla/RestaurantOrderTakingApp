package com.sample.restaurantordertakingapp.ui.theme.screen.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.MenuRepository
import com.sample.restaurantordertakingapp.domain.usecase.cart.AddToCartUseCase
import com.sample.restaurantordertakingapp.domain.usecase.menu.LoadMenuUseCase
import com.sample.restaurantordertakingapp.domain.usecase.order.StartOrderSyncUseCase
import com.sample.restaurantordertakingapp.network.Resource
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toUi
import com.sample.restaurantordertakingapp.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MenuViewModel  @Inject constructor(
    private val loadMenuUseCase: LoadMenuUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    ///private val startOrderSyncUseCase: StartOrderSyncUseCase,
    private val notificationHelper: NotificationHelper
) :ViewModel() {

    private val _menuState = MutableStateFlow<Resource<MenuUi>>(Resource.Loading)
    val menuState: StateFlow<Resource<MenuUi>> = _menuState


    init {
        loadMenu()
    }

   /* fun startListeningForOrders() {
        viewModelScope.launch {
            startOrderSyncUseCase { order ->
                try {
                    notificationHelper.showNewOrderNotification(order.orderNumber)
                } catch (e: SecurityException) {
                    Log.e("OrderSync", "Notification permission denied: ${e.message}")
                }
            }
        }
    }*/

    fun loadMenu() {
        viewModelScope.launch {
            loadMenuUseCase()
                .collect { resource ->
                    _menuState.value  = when (resource) {
                        is Resource.Loading -> Resource.Loading
                        is Resource.Success ->
                            Resource.Success(resource.data.toUi())
                        is Resource.Error ->
                            Resource.Error(resource.message)
                    }
                }
        }
    }


     fun addToCart(item: CartItem) {
        viewModelScope.launch {
            addToCartUseCase(item)
        }
    }
}