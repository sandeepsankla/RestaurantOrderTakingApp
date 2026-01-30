package com.sample.restaurantordertakingapp.ui.theme.screen.address

data class AddressUiState(
    val society: String = "",
    val flatNo: String = "",
    val tower: String = "",
    val mobile: String = "",

    val isLoading: Boolean = false,
    val error: String? = null
)
/**
 * UI-level validation
 * Only mobile is mandatory
 */
fun AddressUiState.isValid(): Boolean {
    return mobile.length == 10
}
fun AddressUiState.mobileError(): String? {
    return if (mobile.isNotEmpty() && mobile.length != 10)
        "Enter valid mobile number"
    else null
}

