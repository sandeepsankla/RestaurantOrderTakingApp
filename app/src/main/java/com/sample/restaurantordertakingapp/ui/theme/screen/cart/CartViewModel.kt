package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.data.model.CartItem
import com.sample.restaurantordertakingapp.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor( private val cartRepo: CartRepository) :ViewModel()  {
    private var _cartItems = MutableStateFlow<Int>(0)
    val cartItems: StateFlow<Int> = _cartItems
   init {
       getCartItemCount()
   }

     fun getCartItemCount(){
         viewModelScope.launch {
             _cartItems = cartRepo.getCartItemsCount() as MutableStateFlow<Int>
         }
    }
    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            cartRepo.addToCart(item)
        }
    }

}
