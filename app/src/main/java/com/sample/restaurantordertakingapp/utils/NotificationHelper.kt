package com.sample.restaurantordertakingapp.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sample.restaurantordertakingapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNewOrderNotification(orderId: String) {

        val channelId = "orders_channel"

        val channel = NotificationChannel(
            channelId,
            "Orders",
            NotificationManager.IMPORTANCE_HIGH
        )
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.outline_add_alert_24)
            .setContentTitle("New Order Received")
            .setContentText("Order #$orderId")
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(orderId.hashCode(), notification)
    }
}
