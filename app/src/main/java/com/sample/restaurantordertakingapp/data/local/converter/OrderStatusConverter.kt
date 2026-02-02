package com.sample.restaurantordertakingapp.data.local.converter

import androidx.room.TypeConverter
import com.sample.restaurantordertakingapp.domain.model.OrderStatus

class OrderStatusConverter {

    @TypeConverter
    fun fromStatus(status: OrderStatus): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }
}
