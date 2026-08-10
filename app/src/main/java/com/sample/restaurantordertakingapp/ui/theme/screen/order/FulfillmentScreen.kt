package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sample.restaurantordertakingapp.ui.theme.component.common.LoadingView

@Composable
fun FulfillmentScreen(
    state: OrdersUiState,
    onRefresh: () -> Unit,
    onAdvance: (String, Int) -> Unit,
    onPay: (String, String, Int) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            when {
                state.isLoading && state.orders.isEmpty() ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingView(PaddingValues(16.dp))
                    }

                state.orders.isEmpty() ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No orders yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                else ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = state.orders, key = { it.orderId }) { order ->
                            FulfillmentCard(order = order, onAdvance = onAdvance, onPay = onPay)
                        }
                    }
            }
        }
    }
}

@Composable
private fun FulfillmentCard(
    order: OrderUi,
    onAdvance: (String, Int) -> Unit,
    onPay: (String, String, Int) -> Unit
) {
    val takeaway = order.isTakeaway
    val step = order.fulfillmentStep
    val paid = order.paymentMethod != null
    var showPayDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Order #${order.orderNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TypeChip(if (takeaway) "Takeaway" else (order.tableLabel ?: "Table"))
            }

            HorizontalDivider()

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                order.items.forEach {
                    Text("${it.quantity}× ${it.name}", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "₹${order.totalAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    order.paymentMethod?.let { PayChip(it) }
                    Text(
                        statusText(takeaway, step, paid),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val label = actionLabel(takeaway, step, paid)
            if (label != null) {
                Button(
                    onClick = {
                        if (isPaymentStep(takeaway, step, paid)) showPayDialog = true
                        else onAdvance(order.orderId, step)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(label) }
            }
        }
    }

    if (showPayDialog) {
        PaymentMethodDialog(
            onPick = { method ->
                onPay(order.orderId, method, step)
                showPayDialog = false
            },
            onDismiss = { showPayDialog = false }
        )
    }
}

@Composable
private fun PaymentMethodDialog(onPick: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Payment kaise mila?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onPick("CASH") }, modifier = Modifier.fillMaxWidth()) { Text("💵 Cash") }
                Button(onClick = { onPick("UPI") }, modifier = Modifier.fillMaxWidth()) { Text("📲 UPI") }
                OutlinedButton(onClick = { onPick("UDHAAR") }, modifier = Modifier.fillMaxWidth()) { Text("📒 Udhaar (baaki)") }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun TypeChip(text: String) {
    Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PayChip(method: String) {
    val label = when (method) {
        "CASH" -> "💵 Cash"
        "UPI" -> "📲 UPI"
        "UDHAAR" -> "📒 Udhaar"
        else -> method
    }
    Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.tertiaryContainer) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun isPaymentStep(takeaway: Boolean, step: Int, paid: Boolean): Boolean =
    !paid && ((takeaway && step == 0) || (!takeaway && step == 1))

private fun actionLabel(takeaway: Boolean, step: Int, paid: Boolean): String? = when {
    // takeaway: (pay if unpaid) -> deliver
    takeaway && step >= 2 -> null
    takeaway && step == 0 && !paid -> "💳 Payment Taken"
    takeaway -> "📦 Mark Delivered"
    // dine-in: serve -> (pay if unpaid)
    step >= 2 -> null
    step >= 1 && paid -> null              // served + already paid = done
    step == 0 -> "🍽 Serving on Table"
    else -> "💳 Payment Collected"
}

private fun statusText(takeaway: Boolean, step: Int, paid: Boolean): String {
    val pay = if (paid) "Paid" else "Unpaid"
    val fulfil = if (takeaway) {
        if (step >= 2) "Delivered" else "To deliver"
    } else {
        if (step >= 1) "Served" else "To serve"
    }
    return "$pay • $fulfil"
}
