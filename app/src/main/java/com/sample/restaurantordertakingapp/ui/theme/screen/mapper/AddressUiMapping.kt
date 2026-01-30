package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressUi
import com.sample.restaurantordertakingapp.ui.theme.screen.address.AddressUiState

fun AddressUiState.toAddressUi(): AddressUi {
    return AddressUi(
        society = society.takeIf { it.isNotBlank() },
        flatNo = flatNo.takeIf { it.isNotBlank() },
        tower = tower.takeIf { it.isNotBlank() },
        mobile = mobile
    )
}
