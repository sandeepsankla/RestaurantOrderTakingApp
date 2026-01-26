package com.sample.restaurantordertakingapp.data.remote.firebase.model

data class MenuItemDocument(
    val id: String = "",
    val name: String = "",
    val halfPrice: Int = 0,
    val fullPrice: Int = 0,
    val description: String? = null,
    val imageUrl: String? = null
)