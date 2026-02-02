package com.sample.restaurantordertakingapp.utils

import java.time.LocalDate


object DateProvider {

    fun today(): String {
        return LocalDate.now().toString() // yyyy-MM-dd
    }
}
