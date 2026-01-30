package com.sample.restaurantordertakingapp.ui.theme.screen.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.usecase.order.PlaceOrderUseCase
import com.sample.restaurantordertakingapp.ui.theme.screen.mapper.toAddressUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val placeOrderUseCase: PlaceOrderUseCase
) : ViewModel() {

    var uiState by mutableStateOf(AddressUiState())
        private set

    fun onSocietyChange(value: String) {
        uiState = uiState.copy(society = value)
    }

    fun onFlatNoChange(value: String) {
        uiState = uiState.copy(flatNo = value)
    }

    fun onTowerChange(value: String) {
        uiState = uiState.copy(tower = value)
    }

    fun onMobileChange(value: String) {
        uiState = uiState.copy(mobile = value.take(10))
    }

    fun onPlaceOrderClick(
        onSuccess: () -> Unit
    ) {
        if (!uiState.isValid()) {
            uiState = uiState.copy(error = "Enter valid mobile number")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            placeOrderUseCase(uiState.toAddressUi())

            uiState = uiState.copy(isLoading = false)
            onSuccess()
        }
    }

    fun saveAddress(addressUi: AddressUi) {

    }
}
