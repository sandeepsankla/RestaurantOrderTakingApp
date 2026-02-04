package com.sample.restaurantordertakingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.FirebaseApp
import com.sample.restaurantordertakingapp.domain.repo.OrderPullRepository
import com.sample.restaurantordertakingapp.utils.CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
public class RestaurantOrderApplication : Application() {
    @Inject
    lateinit var orderPullRepository: OrderPullRepository


    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        createNotificationChannel()
        orderPullRepository.startListening()
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Orders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "New order notifications"
        }

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }

    override fun onTerminate() {
        super.onTerminate()

        // ✅ App exit / logout → stop listening
        orderPullRepository.stopListening()
    }

}