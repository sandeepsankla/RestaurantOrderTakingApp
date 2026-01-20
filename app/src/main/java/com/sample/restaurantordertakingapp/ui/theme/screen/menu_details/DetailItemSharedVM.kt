package com.sample.restaurantordertakingapp.ui.theme.screen.menu_details

import androidx.lifecycle.ViewModel
import com.sample.restaurantordertakingapp.data.repository.CartRepository
import com.sample.restaurantordertakingapp.data.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailItemSharedVM @Inject constructor( private val repo: MenuRepository,  private val cartRepo: CartRepository) :ViewModel() {
}