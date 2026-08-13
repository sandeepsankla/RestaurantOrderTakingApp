package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VoiceOrderParserTest {

    private lateinit var parser: VoiceOrderParser
    private lateinit var sampleMenuItems: List<MenuItemUi>

    @BeforeEach
    fun setUp() {
        parser = VoiceOrderParser()
        sampleMenuItems = listOf(
            MenuItemUi(id = 101, name = "Veg Noodles", halfPrice = 80, fullPrice = 140),
            MenuItemUi(id = 102, name = "Singapori Noodles", halfPrice = 90, fullPrice = 140),
            MenuItemUi(id = 103, name = "Hakka Noodles", halfPrice = 90, fullPrice = 140),
            MenuItemUi(id = 104, name = "Chilly Garlic Noodles", halfPrice = 90, fullPrice = 140),
            MenuItemUi(id = 105, name = "Paneer Butter Chowmein", halfPrice = 100, fullPrice = 150),
            MenuItemUi(id = 106, name = "Veg Fried Rice", halfPrice = 120, fullPrice = 180)
        )
    }

    @Test
    fun parseSpokenCommand_chominSpelling_parsesToVegNoodles() {
        val result = parser.parseSpokenCommand(
            spokenText = "1 chomin",
            allItems = sampleMenuItems
        )

        assertEquals(1, result.size)
        assertEquals("Veg Noodles", result.first().menuItem.name)
    }

    @Test
    fun parseSpokenCommand_chowmeenSpelling_parsesToVegNoodles() {
        val result = parser.parseSpokenCommand(
            spokenText = "1 chowmeen",
            allItems = sampleMenuItems
        )

        assertEquals(1, result.size)
        assertEquals("Veg Noodles", result.first().menuItem.name)
    }

    @Test
    fun parseSpokenCommand_singaporiChomin_parsesToSingaporiNoodles() {
        val result = parser.parseSpokenCommand(
            spokenText = "1 singapori chomin",
            allItems = sampleMenuItems
        )

        assertEquals(1, result.size)
        assertEquals("Singapori Noodles", result.first().menuItem.name)
    }

    @Test
    fun parseSpokenCommand_paneerButterChowmeen_parsesToPaneerButterChowmein() {
        val result = parser.parseSpokenCommand(
            spokenText = "half paneer butter chowmeen",
            allItems = sampleMenuItems
        )

        assertEquals(1, result.size)
        assertEquals("Paneer Butter Chowmein", result.first().menuItem.name)
        assertEquals(PortionType.HALF, result.first().portion)
    }
}
