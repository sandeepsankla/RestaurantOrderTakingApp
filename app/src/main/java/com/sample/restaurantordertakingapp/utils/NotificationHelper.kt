package com.sample.restaurantordertakingapp.utils

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.speech.tts.TextToSpeech
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

// Naya channel id — sound settings tabhi apply hoti hain jab channel pehli baar bane.
const val CHANNEL_ID = "orders_channel_v2"

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Voice announcement — "Naya order aaya"
    private var ttsReady = false
    private val tts = TextToSpeech(context) { status ->
        ttsReady = status == TextToSpeech.SUCCESS
    }

    @SuppressLint("MissingPermission")
    fun showNewOrderNotification(orderNumber: Int) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle("New Order Received")
            .setContentText("Order #$orderNumber")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // pre-Android 8: sound+vibrate+lights
            .setSound(soundUri)                          // pre-Android 8 sound
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(orderNumber, notification)

        announceOrder(orderNumber)
    }

    /** Bolke bataye — "Naya order aaya, order number N". */
    private fun announceOrder(orderNumber: Int) {
        if (!ttsReady) return
        runCatching {
            tts.language = Locale("hi", "IN")   // Hindi; na mile to default voice
            tts.setSpeechRate(0.95f)
            tts.speak(
                "Naya order aaya. Order number $orderNumber",
                TextToSpeech.QUEUE_FLUSH,
                null,
                "order_$orderNumber"
            )
        }
    }
}
