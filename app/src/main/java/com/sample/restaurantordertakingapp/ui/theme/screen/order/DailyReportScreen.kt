package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class DayReport(
    val date: String,
    val orders: Int,
    val total: Int,
    val cash: Int,
    val upi: Int,
    val udhaar: Int
)

@Composable
fun DailyReportScreen(state: OrdersUiState) {
    val report = buildReport(state.orders)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        if (report.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No sales yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = report, key = { it.date }) { day -> DayCard(day) }
            }
        }
    }
}

@Composable
private fun DayCard(day: DayReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(day.date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${day.orders} orders", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            HorizontalDivider()

            ReportRow("💵 Cash collected", day.cash)
            ReportRow("📲 UPI collected", day.upi)
            ReportRow("📒 Udhaar (baaki)", day.udhaar, valueColor = Color(0xFFEF6C00))

            HorizontalDivider()

            ReportRow("Total sale", day.total, bold = true)
        }
    }
}

@Composable
private fun ReportRow(label: String, amount: Int, bold: Boolean = false, valueColor: Color? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            "₹$amount",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold,
            color = valueColor ?: MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun buildReport(orders: List<OrderUi>): List<DayReport> =
    orders
        .filter { it.orderDate.isNotBlank() }
        .groupBy { it.orderDate }
        .map { (date, list) ->
            DayReport(
                date = date,
                orders = list.size,
                total = list.sumOf { it.totalAmount },
                cash = list.filter { it.paymentMethod == "CASH" }.sumOf { it.totalAmount },
                upi = list.filter { it.paymentMethod == "UPI" }.sumOf { it.totalAmount },
                udhaar = list.filter { it.paymentMethod == "UDHAAR" }.sumOf { it.totalAmount }
            )
        }
        .sortedByDescending { it.date }
