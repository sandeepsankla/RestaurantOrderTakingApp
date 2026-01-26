package com.sample.restaurantordertakingapp.data.remote.firebase.model


data class CategoryDocument(
    val id: Int ,
    val name: String = "",
    val items: List<MenuItemDocument> =emptyList<MenuItemDocument>()
)