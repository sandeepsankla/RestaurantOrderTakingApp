package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VoiceOrderParserTest {

    private lateinit var parser: VoiceOrderParser
    private lateinit var sampleMenuItems: List<MenuItemUi>

    @BeforeEach
    fun setUp() {
        parser = VoiceOrderParser()
        sampleMenuItems = listOf(
            MenuItemUi(
                id = 101,
                name = "Chowmein",
                description = "Delicious noodles",
                halfPrice = 80,
                fullPrice = 140,
                imageUrl = null
            ),
            MenuItemUi(
                id = 102,
                name = "Paneer Butter Masala",
                description = "Rich cottage cheese curry",
                halfPrice = 160,
                fullPrice = 280,
                imageUrl = null
            ),
            MenuItemUi(
                id = 103,
                name = "Dal Makhani",
                description = "Creamy lentils",
                halfPrice = 120,
                fullPrice = 200,
                imageUrl = null
            )
        )
    }

    @Test
    fun parseSpokenCommand_singleHalfItemInHinglish_parsesQuantityAndPortionCorrectly() {
        val result = parser.parseSpokenCommand(
            spokenText = "2 half chowmein daal do",
            allItems = sampleMenuItems
        )

        assertEquals(1, result.size)
        val item = result.first()
        assertEquals("Chowmein", item.menuItem.name)
        assertEquals(2, item.quantity)
        assertEquals(PortionType.HALF, item.portion)
    }

    @Test
    fun parseSpokenCommand_multipleItemsWithAurSeparator_parsesAllItems() {
        val result = parser.parseSpokenCommand(
            spokenText = "2 half plate chowmein aur 1 full paneer butter masala",
            allItems = sampleMenuItems
        )

        assertEquals(2, result.size)

        val chowmein = result.find { it.menuItem.name == "Chowmein" }
        assertNotNull(chowmein)
        assertEquals(2, chowmein.quantity)
        assertEquals(PortionType.HALF, chowmein.portion)

        val paneer = result.find { it.menuItem.name == "Paneer Butter Masala" }
        assertNotNull(paneer)
        assertEquals(1, paneer.quantity)
        assertEquals(PortionType.FULL, paneer.portion)
    }

    @Test
    fun parseSpokenCommand_unrecognizedText_returnsEmptyList() {
        val result = parser.parseSpokenCommand(
            spokenText = "kuch bhi Random words hello world",
            allItems = sampleMenuItems
        )

        assertTrue(result.isEmpty())
    }
}
