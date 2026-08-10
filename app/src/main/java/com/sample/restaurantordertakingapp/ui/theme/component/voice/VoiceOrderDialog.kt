package com.sample.restaurantordertakingapp.ui.theme.component.voice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import com.sample.restaurantordertakingapp.utils.SpeechRecognizerHelper
import com.sample.restaurantordertakingapp.utils.VoiceOrderParser
import com.sample.restaurantordertakingapp.utils.VoiceParsedItem
import com.sample.restaurantordertakingapp.utils.VoiceState

@Composable
fun VoiceOrderDialog(
    showDialog: Boolean,
    allItems: List<MenuItemUi>,
    onDismiss: () -> Unit,
    onConfirmAddItems: (List<VoiceParsedItem>) -> Unit
) {
    if (!showDialog) return

    val context = LocalContext.current
    val speechHelper = remember { SpeechRecognizerHelper(context) }
    val parser = remember { VoiceOrderParser() }
    val voiceState by speechHelper.voiceState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechHelper.startListening()
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            speechHelper.startListening()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechHelper.stopListening()
        }
    }

    Dialog(onDismissRequest = {
        speechHelper.stopListening()
        onDismiss()
    }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎙️ Voice Order",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Microphone Pulse Animation
                val infiniteTransition = rememberInfiniteTransition(label = "micPulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 1.0f,
                    targetValue = if (voiceState is VoiceState.Listening) 1.25f else 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(90.dp)
                ) {
                    if (voiceState is VoiceState.Listening) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .scale(pulseScale)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                        )
                    }

                    IconButton(
                        onClick = {
                            if (voiceState is VoiceState.Listening) {
                                speechHelper.stopListening()
                            } else {
                                speechHelper.startListening()
                            }
                        },
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                if (voiceState is VoiceState.Listening)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (voiceState is VoiceState.Listening) Icons.Default.Mic else Icons.Default.MicOff,
                            contentDescription = "Mic",
                            tint = if (voiceState is VoiceState.Listening) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Message
                val statusText = when (val state = voiceState) {
                    is VoiceState.Listening -> "Listening... Please speak (e.g. '2 half chowmein aur 1 full paneer butter masala')"
                    is VoiceState.Idle -> "Tap microphone to speak"
                    is VoiceState.Success -> "Recognized Command:"
                    is VoiceState.Error -> state.message
                    else -> "Tap microphone to speak"
                }

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = if (voiceState is VoiceState.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Show Spoken Result & Parsed Items
                if (voiceState is VoiceState.Success) {
                    val spokenText = (voiceState as VoiceState.Success).spokenText

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"$spokenText\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val parsedItems = parser.parseSpokenCommand(spokenText, allItems)

                    if (parsedItems.isNotEmpty()) {
                        Text(
                            text = "Items Matched:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            items(parsedItems) { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = item.menuItem.name,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Qty: ${item.quantity} | Portion: ${item.portion.name}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = "₹${if (item.portion == PortionType.FULL) item.menuItem.fullPrice * item.quantity else item.menuItem.halfPrice * item.quantity}",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onConfirmAddItems(parsedItems)
                                speechHelper.stopListening()
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add ${parsedItems.size} Item(s) to Cart")
                        }
                    } else {
                        Text(
                            text = "No matching items found in menu. Try speaking item name clearly.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        speechHelper.stopListening()
                        onDismiss()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Close")
                }
            }
        }
    }
}
