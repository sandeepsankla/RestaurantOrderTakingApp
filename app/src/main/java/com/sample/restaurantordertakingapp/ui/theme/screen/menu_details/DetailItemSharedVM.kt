package com.sample.restaurantordertakingapp.ui.theme.screen.menu_details

import androidx.lifecycle.ViewModel
import com.sample.restaurantordertakingapp.data.repository.CartRepositoryImpl
import com.sample.restaurantordertakingapp.data.repository.MenuRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailItemSharedVM @Inject constructor(private val repo: MenuRepositoryImpl, private val cartRepo: CartRepositoryImpl) :ViewModel() {
}