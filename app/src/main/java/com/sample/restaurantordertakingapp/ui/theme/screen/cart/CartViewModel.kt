package com.sample.restaurantordertakingapp.ui.theme.screen.cart

import androidx.lifecycle.ViewModel
import com.sample.restaurantordertakingapp.data.repository.CartRepository
import javax.inject.Inject

class CartViewModel@Inject constructor( private val cartRepo: CartRepository) :ViewModel()  {


}
