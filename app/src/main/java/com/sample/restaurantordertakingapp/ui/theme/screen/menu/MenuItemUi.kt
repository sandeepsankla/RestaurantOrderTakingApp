package com.sample.restaurantordertakingapp.ui.theme.screen.menu

import com.sample.restaurantordertakingapp.domain.model.PortionType

data class MenuItemUi(
    val id: Int = 1,
    val name: String = "",
    val halfPrice: Int = 0,
    val fullPrice: Int = 0,
    val description: String? = null,
    val imageUrl: String? = null
    //val portion : PortionType
){
   fun getFormattedPrice(): String {
       return "Rs${if (fullPrice > 0) fullPrice else halfPrice}"
   }
}