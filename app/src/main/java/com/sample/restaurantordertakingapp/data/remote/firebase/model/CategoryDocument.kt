package com.sample.restaurantordertakingapp.data.remote.firebase.model


data class CategoryDocument(
    val id: Int=0,
    val name: String = "",
    val items: List<MenuItemDocument> =emptyList<MenuItemDocument>()
)