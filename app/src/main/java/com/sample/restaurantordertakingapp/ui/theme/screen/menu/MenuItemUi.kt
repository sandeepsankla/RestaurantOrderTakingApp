package com.sample.restaurantordertakingapp.ui.theme.screen.menu

import com.sample.restaurantordertakingapp.domain.model.PortionType

data class MenuItemUi(
    val id: Int = 1,
    val name: String = "",
    val halfPrice: Int = 0,
    val fullPrice: Int = 0,
    val description: String? = null,
    val imageUrl: String? = null,
    val isFull :Boolean = true
    //val portion : PortionType
){
    /**
     * Returns the unit price based on whether 'Full' or 'Half' is selected.
     */
    fun getUnitPrice(isFull: Boolean): Int {
        return if (isFull) fullPrice else halfPrice
    }

    /**
     * Calculates the total price based on selection and quantity.
     */
    fun calculateTotal(isFull: Boolean, quantity: Int): Int {
        return getUnitPrice(isFull) * quantity
    }

    /**
     * Returns a formatted string for the radio buttons (e.g., "Half - Rs 60")
     */
    fun getFormattedOptionLabel(isFull: Boolean): String {
        val label = if (isFull) "Full" else "Half"
        val price = getUnitPrice(isFull)
        return "$label - Rs $price"
    }

    fun getFormattedTotalPrice(isFull: Boolean, quantity: Int): String {
        val unitPrice = if (isFull) fullPrice else halfPrice
        val total = unitPrice * quantity

        // Using .toInt() if you don't want decimals,
        // or String.format("%.2f") if you need precision
        return "Rs ${total.toInt()}"
    }
}