package com.sample.restaurantordertakingapp.ui.theme.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.repo.CartRepository
import com.sample.restaurantordertakingapp.domain.repo.MenuRepository
import com.sample.restaurantordertakingapp.domain.usecase.menu.LoadMenuUseCase
import com.sample.restaurantordertakingapp.network.Resource
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MenuViewModel  @Inject constructor(
    private val loadMenuUseCase: LoadMenuUseCase,
    private val cartRepo: CartRepository) :ViewModel() {

    private val _menuState = MutableStateFlow<Resource<MenuUi>>(Resource.Loading)
    val menuState: StateFlow<Resource<MenuUi>> = _menuState


    init {
        loadMenu()
    }


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



    /*fun makeOrder(tableId: String, items: List<OrderItemUi>) {
        viewModelScope.launch {
            repo.submitOrder(tableId, items).collect { result ->
                // react to result: show success or error
            }
        }
    }*/

     fun addToCart(item: CartItem) {
        viewModelScope.launch {
          cartRepo.addToCart(item)
        }
    }
}