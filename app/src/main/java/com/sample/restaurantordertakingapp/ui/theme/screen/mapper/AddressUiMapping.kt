package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressUiState

fun AddressUiState.toAddressUi(): Address {
    return Address(
        society = society.takeIf { it.isNotBlank() },
        flat = flatNo.takeIf { it.isNotBlank() },
        tower = tower.takeIf { it.isNotBlank() },
        mobileNo = mobile.toInt()
    )
}


