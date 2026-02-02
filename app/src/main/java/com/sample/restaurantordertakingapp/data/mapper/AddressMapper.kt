package com.sample.restaurantordertakingapp.data.mapper

import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity
import com.sample.restaurantordertakingapp.domain.model.Address

fun Address.toEntity(orderId: String): AddressEntity =
    AddressEntity(
        orderId = orderId,
        society = society,
        flatNo = flatNo,
        tower = tower,
        mobile = mobile
    )
