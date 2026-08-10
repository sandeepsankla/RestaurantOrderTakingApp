package com.sample.restaurantordertakingapp.ui.theme.screen.call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** RECEPTION: "kya hua?" — preset ya custom message bhejo. */
@Composable
fun CallKitchenDialog(
    onSend: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    val presets = listOf("Jaldi karo 🙏", "Order ready?", "Counter pe aao")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Call Kitchen — kya hua?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                presets.forEach { p ->
                    OutlinedButton(
                        onClick = { onSend(p) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(p) }
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Custom message") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { if (text.isNotBlank()) onSend(text) }) { Text("Send") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

/** KITCHEN: incoming call — acknowledge karna zaroori. */
@Composable
fun IncomingCallDialog(
    message: String,
    onAck: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* force ack */ },
        title = { Text("🔔 Reception calling!") },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        confirmButton = {
            Button(onClick = onAck) { Text("OK, aa raha hoon") }
        }
    )
}
