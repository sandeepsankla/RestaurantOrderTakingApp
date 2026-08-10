package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.ui.theme.screen.cart.CartItemUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import javax.inject.Inject

data class VoiceParsedItem(
    val menuItem: MenuItemUi,
    val quantity: Int,
    val portion: PortionType
) {
    fun toCartItemUi(): CartItemUi {
        return CartItemUi(
            id = 0,
            menuItemId = menuItem.id,
            name = menuItem.name,
            imageUrl = menuItem.imageUrl,
            quantity = quantity,
            fullPrice = menuItem.fullPrice,
            halfPrice = menuItem.halfPrice,
            selectedPortion = portion,
            tableText = ""
        )
    }
}

class VoiceOrderParser @Inject constructor() {

    fun parseSpokenCommand(
        spokenText: String,
        allItems: List<MenuItemUi>
    ): List<VoiceParsedItem> {
        val result = mutableListOf<VoiceParsedItem>()
        if (spokenText.isBlank() || allItems.isEmpty()) return result

        val lowercaseInput = spokenText.lowercase()

        // Split spoken sentence by common phrase separators: "aur", "and", "phir", ",", "."
        val segments = lowercaseInput.split(Regex("\\baur\\b|\\band\\b|\\bphir\\b|,|\\."))

        for (segment in segments) {
            val trimmedSegment = segment.trim()
            if (trimmedSegment.isEmpty()) continue

            // 1. Detect Portion Type
            val portion = when {
                trimmedSegment.contains("half") ||
                trimmedSegment.contains("haaf") ||
                trimmedSegment.contains("हाफ") ||
                trimmedSegment.contains("हाफ़") -> PortionType.HALF
                else -> PortionType.FULL
            }

            // 2. Detect Quantity
            val quantity = parseQuantity(trimmedSegment)

            // 3. Find Best Matched Menu Item
            val matchedItem = findBestMenuItemMatch(trimmedSegment, allItems)

            if (matchedItem != null) {
                // Avoid adding duplicate parsed item in single voice command
                if (result.none { it.menuItem.id == matchedItem.id }) {
                    result.add(
                        VoiceParsedItem(
                            menuItem = matchedItem,
                            quantity = quantity,
                            portion = portion
                        )
                    )
                }
            }
        }

        return result
    }

    private fun parseQuantity(text: String): Int {
        // Match numbers in digits
        val digitMatch = Regex("\\b(\\d+)\\b").find(text)
        if (digitMatch != null) {
            return digitMatch.groupValues[1].toIntOrNull()?.coerceAtLeast(1) ?: 1
        }

        // Match Hindi / English number words
        return when {
            text.contains("ek") || text.contains("एक") || text.contains("one") || text.contains("single") -> 1
            text.contains("do") || text.contains("दो") || text.contains("two") || text.contains("double") -> 2
            text.contains("teen") || text.contains("तीन") || text.contains("three") || text.contains("triple") -> 3
            text.contains("chaar") || text.contains("char") || text.contains("चार") || text.contains("four") -> 4
            text.contains("paanch") || text.contains("panch") || text.contains("पांच") || text.contains("five") -> 5
            else -> 1
        }
    }

    private fun findBestMenuItemMatch(text: String, allItems: List<MenuItemUi>): MenuItemUi? {
        var bestItem: MenuItemUi? = null
        var maxScore = 0

        for (item in allItems) {
            val itemNameLower = item.name.lowercase()
            val words = itemNameLower.split(" ")

            var matchScore = 0
            for (word in words) {
                if (word.length >= 3 && text.contains(word)) {
                    matchScore += word.length
                }
            }

            // Full item name match gets highest priority
            if (text.contains(itemNameLower)) {
                matchScore += 50
            }

            if (matchScore > maxScore && matchScore >= 3) {
                maxScore = matchScore
                bestItem = item
            }
        }

        return bestItem
    }
}
