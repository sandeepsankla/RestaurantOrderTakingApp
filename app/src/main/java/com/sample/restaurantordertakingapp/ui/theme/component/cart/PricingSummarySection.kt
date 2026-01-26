package com.sample.restaurantordertakingapp.ui.theme.component.cart

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.sample.restaurantordertakingapp.domain.model.CartState


// Components/PricingSummarySection.kt
@Composable
fun PricingSummarySection(
    subtotal: Double,
    tax: Double,
    total: Double
) {
    Column(modifier = Modifier.padding(16.dp)) {
        PriceRow("Subtotal", subtotal)
        PriceRow("GST (5%)", tax)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        PriceRow("Total", total, isTotal = true)
    }
}


@Composable
fun PriceRow(
    label: String,
    amount: Double,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "$${"%.2f".format(amount)}",
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
    }
}