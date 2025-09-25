package com.sample.restaurantordertakingapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
public class RestaurantOrderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}