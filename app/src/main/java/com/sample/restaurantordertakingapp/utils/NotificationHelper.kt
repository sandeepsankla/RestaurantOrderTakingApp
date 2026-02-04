package com.sample.restaurantordertakingapp.utils

import android.Manifest
import android.R
import android.annotation.SuppressLint

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject



const val CHANNEL_ID = "orders_channel"
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @SuppressLint("MissingPermission")
    fun showNewOrderNotification(orderNumber: Int) {

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_dialog_info)
                .setContentTitle("New Order Received")
                .setContentText("Order #$orderNumber")
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat
                .from(context)
                .notify(orderNumber, notification)
        }
    }

