package com.sample.restaurantordertakingapp.ui.theme.component.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*


@Composable
fun TakeawayAddressSection(
    society: String,
    onSocietyChange: (String) -> Unit,

    flatNo: String,
    onFlatNoChange: (String) -> Unit,

    tower: String,
    onTowerChange: (String) -> Unit,

    mobileNo: String,
    onMobileNoChange: (String) -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {

        Text("Society")
        OutlinedTextField(
            value = society,
            onValueChange = onSocietyChange,
            placeholder = { Text("Enter society") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Text("Flat No")
        OutlinedTextField(
            value = flatNo,
            onValueChange = onFlatNoChange,
            placeholder = { Text("Enter flat no") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Text("Tower")
        OutlinedTextField(
            value = tower,
            onValueChange = onTowerChange,
            placeholder = { Text("Enter tower") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Text("Mobile No")
        OutlinedTextField(
            value = mobileNo,
            onValueChange = { onMobileNoChange(it.filter(Char::isDigit)) },
            placeholder = { Text("Enter mobile no") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = true,
                onCheckedChange = {
                    //todo later
                }
            )
            Text("Save Address")
        }
    }
}
