package com.sample.restaurantordertakingapp.ui.theme.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.data.model.OrderItem
import com.sample.restaurantordertakingapp.data.repository.MenuRepository
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel  @Inject constructor( private val repo: MenuRepository) :ViewModel() {

    private val _menuState = MutableStateFlow<Resource<List<MenuItem>>>(Resource.Loading)
    val menuState: StateFlow<Resource<List<MenuItem>>> = _menuState

    fun loadMenu() {
        viewModelScope.launch {
            repo.fetchMenu().collect { _menuState.value = it }
        }
    }

    fun makeOrder(tableId: String, items: List<OrderItem>) {
        viewModelScope.launch {
            repo.submitOrder(tableId, items).collect { result ->
                // react to result: show success or error
            }
        }
    }
}