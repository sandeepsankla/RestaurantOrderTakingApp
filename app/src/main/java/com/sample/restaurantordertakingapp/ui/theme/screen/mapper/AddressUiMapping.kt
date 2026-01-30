package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.Address
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressUiState

fun AddressUiState.toAddressUi(): Address {
    return Address(
        society = society.takeIf { it.isNotBlank() },
        flatNo = flatNo.takeIf { it.isNotBlank() },
        tower = tower.takeIf { it.isNotBlank() },
        mobile = mobile,
        orderId = ""
    )
}


