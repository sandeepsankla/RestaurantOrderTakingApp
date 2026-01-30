package com.sample.restaurantordertakingapp.ui.theme.screen.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.ui.theme.component.cart.TakeawayAddressSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    state: AddressUiState,
    onSocietyChange: (String) -> Unit,
    onFlatNoChange: (String) -> Unit,
    onTowerChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit
) {
    when {
        state.isSuccess -> {
            // ✅ SUCCESS UI
            AddressSuccessView()
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                TakeawayAddressSection(
                    society = state.society,
                    onSocietyChange = onSocietyChange,
                    flatNo = state.flatNo,
                    onFlatNoChange = onFlatNoChange,
                    tower = state.tower,
                    onTowerChange = onTowerChange,
                    mobileNo = state.mobile,
                    onMobileNoChange = onMobileChange,
                )

                Spacer(Modifier.height(24.dp))

                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Button(
                    onClick = onPlaceOrder,
                    enabled = state.isValid() && !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Place Order")
                    }
                }
            }
        }
    }
}
