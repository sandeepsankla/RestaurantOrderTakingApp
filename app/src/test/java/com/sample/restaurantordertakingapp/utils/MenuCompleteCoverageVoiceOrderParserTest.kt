package com.sample.restaurantordertakingapp.utils

import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MenuCompleteCoverageVoiceOrderParserTest {

    private lateinit var parser: VoiceOrderParser
    private lateinit var fullMenuItems: List<MenuItemUi>

    @BeforeEach
    fun setUp() {
        parser = VoiceOrderParser()
        fullMenuItems = listOf(
            // Noodles
            MenuItemUi(id = 101, name = "Veg Noodles", halfPrice = 80, fullPrice = 120),
            MenuItemUi(id = 102, name = "Singapori Noodles", halfPrice = 90, fullPrice = 140),
            MenuItemUi(id = 103, name = "Hakka Noodles", halfPrice = 90, fullPrice = 140),
            MenuItemUi(id = 104, name = "Chilly Garlic Noodles", halfPrice = 90, fullPrice = 140),
            MenuItemUi(id = 105, name = "Paneer Butter Chowmein", halfPrice = 100, fullPrice = 150),
            
            // Momos
            MenuItemUi(id = 201, name = "Veg Steam Momos", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 202, name = "Veg Fried Momos", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 203, name = "Veg Kurkure Momos", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 204, name = "Paneer Steam Momos", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 205, name = "Paneer Fried Momos", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 206, name = "Paneer Kurkure Momos", halfPrice = 140, fullPrice = 140),
            MenuItemUi(id = 207, name = "Veg Tandoori Momos", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 208, name = "Paneer Tandoori Momos", halfPrice = 140, fullPrice = 140),
            
            // Tandoori Snacks
            MenuItemUi(id = 301, name = "Malai Chap", halfPrice = 120, fullPrice = 170),
            MenuItemUi(id = 302, name = "Masala Chap", halfPrice = 120, fullPrice = 170),
            MenuItemUi(id = 303, name = "Afgani Chap", halfPrice = 130, fullPrice = 180),
            MenuItemUi(id = 304, name = "Achari Chaap", halfPrice = 120, fullPrice = 170),
            MenuItemUi(id = 305, name = "Malai Masala Chaap", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 306, name = "Paneer Tikka", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 307, name = "Malai Paneer Tikka", halfPrice = 160, fullPrice = 210),
            MenuItemUi(id = 308, name = "Mushrom Tikka", halfPrice = 150, fullPrice = 200),
            
            // Chinese Snacks
            MenuItemUi(id = 401, name = "Chilly potato", halfPrice = 130, fullPrice = 130),
            MenuItemUi(id = 402, name = "Honey Chilly Potato", halfPrice = 140, fullPrice = 140),
            MenuItemUi(id = 403, name = "French Fries", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 404, name = "Chilly Panner Gravy", halfPrice = 200, fullPrice = 200),
            MenuItemUi(id = 405, name = "Chilly Paneer Dry", halfPrice = 220, fullPrice = 220),
            MenuItemUi(id = 406, name = "Manchurian Gravy", halfPrice = 150, fullPrice = 150),
            MenuItemUi(id = 407, name = "Manchurian Dry", halfPrice = 170, fullPrice = 170),
            MenuItemUi(id = 408, name = "Chilly Mushroom", halfPrice = 190, fullPrice = 190),
            MenuItemUi(id = 409, name = "Crispy Corn", halfPrice = 150, fullPrice = 150),
            
            // Rice Combos
            MenuItemUi(id = 501, name = "Veg Fried Rice", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 502, name = "Paneer Fried Rice", halfPrice = 150, fullPrice = 150),
            MenuItemUi(id = 503, name = "Manchurian + Fried Rice", halfPrice = 190, fullPrice = 190),
            MenuItemUi(id = 504, name = "Manchurian + Noodles", halfPrice = 180, fullPrice = 180),
            MenuItemUi(id = 505, name = "Chilly Paneer + Fried Rice", halfPrice = 200, fullPrice = 200),
            MenuItemUi(id = 506, name = "Chilly Paneer + Noodles", halfPrice = 200, fullPrice = 200),
            
            // Burgers & Rolls
            MenuItemUi(id = 601, name = "Aloo Tikki Burger", halfPrice = 50, fullPrice = 50),
            MenuItemUi(id = 602, name = "Paneer Burger", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 603, name = "Veg Cheese Burger", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 604, name = "Paneer Cheese Burger", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 605, name = "Spring Roll", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 606, name = "Chaap Roll", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 607, name = "Paneer Tikka Roll", halfPrice = 140, fullPrice = 140),
            MenuItemUi(id = 608, name = "Kathi Veg Roll", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 609, name = "Kathi Paneer Roll", halfPrice = 120, fullPrice = 120),
            
            // Main Course
            MenuItemUi(id = 701, name = "Dal Tadka", halfPrice = 80, fullPrice = 130),
            MenuItemUi(id = 702, name = "Dal Makhni", halfPrice = 130, fullPrice = 180),
            MenuItemUi(id = 703, name = "Aloo Jeera", halfPrice = 80, fullPrice = 120),
            MenuItemUi(id = 704, name = "Aloo Matar", halfPrice = 100, fullPrice = 150),
            MenuItemUi(id = 705, name = "Aloo Gobhi", halfPrice = 100, fullPrice = 150),
            MenuItemUi(id = 706, name = "Chana Masala", halfPrice = 100, fullPrice = 150),
            MenuItemUi(id = 707, name = "Rajma Masala", halfPrice = 100, fullPrice = 150),
            MenuItemUi(id = 708, name = "Dum Aloo", halfPrice = 150, fullPrice = 150),
            MenuItemUi(id = 709, name = "Mix Veg", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 710, name = "Malai Kofta", halfPrice = 200, fullPrice = 200),
            MenuItemUi(id = 711, name = "Matar Mushroom", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 712, name = "Kadai Mushroom", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 713, name = "Twa Chap Gravy", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 714, name = "Shahi chap", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 715, name = "Kadai Chap", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 716, name = "Sev Bhaji", halfPrice = 100, fullPrice = 150),
            
            // Breads
            MenuItemUi(id = 801, name = "Tandoori Butter Roti", halfPrice = 12, fullPrice = 12),
            MenuItemUi(id = 802, name = "Missi Roti", halfPrice = 30, fullPrice = 30),
            MenuItemUi(id = 803, name = "Pyaz Roti", halfPrice = 15, fullPrice = 15),
            MenuItemUi(id = 804, name = "Rumali Roti", halfPrice = 15, fullPrice = 15),
            MenuItemUi(id = 805, name = "Lachha Pararha", halfPrice = 30, fullPrice = 30),
            MenuItemUi(id = 806, name = "Aloo pyaz Paratha", halfPrice = 60, fullPrice = 60),
            MenuItemUi(id = 807, name = "Gobhi Paratha", halfPrice = 60, fullPrice = 60),
            MenuItemUi(id = 808, name = "Paneer Paratha", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 809, name = "Mix Paratha", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 810, name = "Green Chilly paratha", halfPrice = 50, fullPrice = 50),
            MenuItemUi(id = 811, name = "Butter Naan", halfPrice = 40, fullPrice = 40),
            MenuItemUi(id = 812, name = "Garlic Naan", halfPrice = 60, fullPrice = 60),
            MenuItemUi(id = 813, name = "Paneer Naan", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 814, name = "Mix Naan", halfPrice = 80, fullPrice = 80),
            
            // Paneer Specials
            MenuItemUi(id = 901, name = "Shahi Paneer", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 902, name = "Kadai Paneer", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 903, name = "Matar Paneer", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 904, name = "Palak Paneer", halfPrice = 140, fullPrice = 190),
            MenuItemUi(id = 905, name = "Paneer butter Masala", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 906, name = "Paneer Lababdar", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 907, name = "Paneer 2 Payaja", halfPrice = 150, fullPrice = 200),
            MenuItemUi(id = 908, name = "Paneer Bhurji", halfPrice = 200, fullPrice = 200),
            MenuItemUi(id = 909, name = "Paneer Kasturi", halfPrice = 200, fullPrice = 200),
            MenuItemUi(id = 910, name = "Shahi Kajui Paneer", halfPrice = 210, fullPrice = 210),
            
            // Raita & Salad
            MenuItemUi(id = 1001, name = "Boondi Raita", halfPrice = 80, fullPrice = 80),
            MenuItemUi(id = 1002, name = "Mix Raita", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 1003, name = "Peanut masala", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 1004, name = "Masala Papad", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 1005, name = "Green Salad", halfPrice = 100, fullPrice = 100),
            
            // Rice Specials
            MenuItemUi(id = 1101, name = "Jeera Rice", halfPrice = 100, fullPrice = 100),
            MenuItemUi(id = 1102, name = "Veg Pulav", halfPrice = 130, fullPrice = 130),
            MenuItemUi(id = 1103, name = "Matar Pulav", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 1104, name = "Veg Briyani", halfPrice = 150, fullPrice = 150),
            MenuItemUi(id = 1105, name = "Soya chaap Briyani", halfPrice = 160, fullPrice = 160),
            MenuItemUi(id = 1106, name = "Shahi Paneer + Chawal", halfPrice = 180, fullPrice = 180),
            MenuItemUi(id = 1107, name = "Palak Paneer + Chawal", halfPrice = 180, fullPrice = 180),
            MenuItemUi(id = 1108, name = "Rajma/Chole + Chawal", halfPrice = 150, fullPrice = 150),
            
            // Thalis
            MenuItemUi(id = 1201, name = "Thali 1 - Dal Tadka + Shahi Paneer", halfPrice = 120, fullPrice = 120),
            MenuItemUi(id = 1202, name = "Thali 2 - Dal Tadka + Shahi Paneer", halfPrice = 150, fullPrice = 150),
            MenuItemUi(id = 1203, name = "Thali 3 - Paneer Butter Masala + Dal Makhni", halfPrice = 210, fullPrice = 210)
        )
    }

    @Test
    fun testEveryMenuItemInEnglishAndHindiWithFullAndHalfPortions() {
        data class TestCase(
            val input: String,
            val expectedItemName: String,
            val expectedPortion: PortionType
        )

        val testCases = listOf(
            // Noodles
            TestCase("full veg noodles", "Veg Noodles", PortionType.FULL),
            TestCase("half veg noodles", "Veg Noodles", PortionType.HALF),
            TestCase("फुल वेज नूडल्स", "Veg Noodles", PortionType.FULL),
            TestCase("हाफ वेज नूडल्स", "Veg Noodles", PortionType.HALF),

            TestCase("full singapori noodles", "Singapori Noodles", PortionType.FULL),
            TestCase("half singapori noodles", "Singapori Noodles", PortionType.HALF),
            TestCase("फुल सिंगापुरी नूडल्स", "Singapori Noodles", PortionType.FULL),
            TestCase("हाफ सिंगापुरी नूडल्स", "Singapori Noodles", PortionType.HALF),

            TestCase("full hakka noodles", "Hakka Noodles", PortionType.FULL),
            TestCase("half hakka noodles", "Hakka Noodles", PortionType.HALF),
            TestCase("फुल हक्का नूडल्स", "Hakka Noodles", PortionType.FULL),
            TestCase("हाफ हक्का नूडल्स", "Hakka Noodles", PortionType.HALF),

            TestCase("full chilly garlic noodles", "Chilly Garlic Noodles", PortionType.FULL),
            TestCase("half chilly garlic noodles", "Chilly Garlic Noodles", PortionType.HALF),
            TestCase("फुल चिली गार्लिक नूडल्स", "Chilly Garlic Noodles", PortionType.FULL),
            TestCase("हाफ चिली गार्लिक नूडल्स", "Chilly Garlic Noodles", PortionType.HALF),

            TestCase("full paneer butter chowmein", "Paneer Butter Chowmein", PortionType.FULL),
            TestCase("half paneer butter chowmein", "Paneer Butter Chowmein", PortionType.HALF),
            TestCase("फुल पनीर बटर चाउमीन", "Paneer Butter Chowmein", PortionType.FULL),
            TestCase("हाफ पनीर बटर चाउमीन", "Paneer Butter Chowmein", PortionType.HALF),

            // Momos
            TestCase("full veg steam momos", "Veg Steam Momos", PortionType.FULL),
            TestCase("half veg steam momos", "Veg Steam Momos", PortionType.HALF),
            TestCase("फुल वेज स्टीम मोमो", "Veg Steam Momos", PortionType.FULL),
            TestCase("हाफ वेज स्टीम मोमो", "Veg Steam Momos", PortionType.HALF),

            TestCase("full veg fried momos", "Veg Fried Momos", PortionType.FULL),
            TestCase("half veg fried momos", "Veg Fried Momos", PortionType.HALF),
            TestCase("फुल वेज फ्राइड मोमो", "Veg Fried Momos", PortionType.FULL),
            TestCase("हाफ वेज फ्राइड मोमो", "Veg Fried Momos", PortionType.HALF),

            TestCase("full paneer steam momos", "Paneer Steam Momos", PortionType.FULL),
            TestCase("half paneer steam momos", "Paneer Steam Momos", PortionType.HALF),
            TestCase("फुल पनीर स्टीम मोमो", "Paneer Steam Momos", PortionType.FULL),
            TestCase("हाफ पनीर स्टीम मोमो", "Paneer Steam Momos", PortionType.HALF),

            // Tandoori Snacks
            TestCase("full malai chap", "Malai Chap", PortionType.FULL),
            TestCase("half malai chap", "Malai Chap", PortionType.HALF),
            TestCase("फुल मलाई चाप", "Malai Chap", PortionType.FULL),
            TestCase("हाफ मलाई चाप", "Malai Chap", PortionType.HALF),

            TestCase("full masala chap", "Masala Chap", PortionType.FULL),
            TestCase("half masala chap", "Masala Chap", PortionType.HALF),
            TestCase("फुल मसाला चाप", "Masala Chap", PortionType.FULL),
            TestCase("हाफ मसाला चाप", "Masala Chap", PortionType.HALF),

            TestCase("full afgani chap", "Afgani Chap", PortionType.FULL),
            TestCase("half afgani chap", "Afgani Chap", PortionType.HALF),
            TestCase("फुल अफगानी चाप", "Afgani Chap", PortionType.FULL),
            TestCase("हाफ अफगानी चाप", "Afgani Chap", PortionType.HALF),

            TestCase("full paneer tikka", "Paneer Tikka", PortionType.FULL),
            TestCase("half paneer tikka", "Paneer Tikka", PortionType.HALF),
            TestCase("फुल पनीर टिक्का", "Paneer Tikka", PortionType.FULL),
            TestCase("हाफ पनीर टिक्का", "Paneer Tikka", PortionType.HALF),

            // Chinese Snacks
            TestCase("full chilly potato", "Chilly potato", PortionType.FULL),
            TestCase("half chilly potato", "Chilly potato", PortionType.HALF),
            TestCase("फुल चिली पोटैटो", "Chilly potato", PortionType.FULL),
            TestCase("हाफ चिली पोटैटो", "Chilly potato", PortionType.HALF),

            TestCase("full manchurian gravy", "Manchurian Gravy", PortionType.FULL),
            TestCase("half manchurian gravy", "Manchurian Gravy", PortionType.HALF),
            TestCase("फुल मंचूरियन ग्रेवी", "Manchurian Gravy", PortionType.FULL),
            TestCase("हाफ मंचूरियन ग्रेवी", "Manchurian Gravy", PortionType.HALF),

            // Main Course
            TestCase("full dal makhni", "Dal Makhni", PortionType.FULL),
            TestCase("half dal makhni", "Dal Makhni", PortionType.HALF),
            TestCase("फुल दाल मखनी", "Dal Makhni", PortionType.FULL),
            TestCase("हाफ दाल मखनी", "Dal Makhni", PortionType.HALF),

            TestCase("full dal tadka", "Dal Tadka", PortionType.FULL),
            TestCase("half dal tadka", "Dal Tadka", PortionType.HALF),
            TestCase("फुल दाल तड़का", "Dal Tadka", PortionType.FULL),
            TestCase("हाफ दाल तड़का", "Dal Tadka", PortionType.HALF),

            TestCase("full mix veg", "Mix Veg", PortionType.FULL),
            TestCase("half mix veg", "Mix Veg", PortionType.HALF),
            TestCase("फुल मिक्स वेज", "Mix Veg", PortionType.FULL),
            TestCase("हाफ मिक्स वेज", "Mix Veg", PortionType.HALF),

            // Paneer Specials
            TestCase("full shahi paneer", "Shahi Paneer", PortionType.FULL),
            TestCase("half shahi paneer", "Shahi Paneer", PortionType.HALF),
            TestCase("फुल शाही पनीर", "Shahi Paneer", PortionType.FULL),
            TestCase("हाफ शाही पनीर", "Shahi Paneer", PortionType.HALF),

            TestCase("full kadai paneer", "Kadai Paneer", PortionType.FULL),
            TestCase("half kadai paneer", "Kadai Paneer", PortionType.HALF),
            TestCase("फुल कढाई पनीर", "Kadai Paneer", PortionType.FULL),
            TestCase("हाफ कढाई पनीर", "Kadai Paneer", PortionType.HALF),

            TestCase("full matar paneer", "Matar Paneer", PortionType.FULL),
            TestCase("half matar paneer", "Matar Paneer", PortionType.HALF),
            TestCase("फुल मटर पनीर", "Matar Paneer", PortionType.FULL),
            TestCase("हाफ मटर पनीर", "Matar Paneer", PortionType.HALF),

            TestCase("full palak paneer", "Palak Paneer", PortionType.FULL),
            TestCase("half palak paneer", "Palak Paneer", PortionType.HALF),
            TestCase("फुल पालक पनीर", "Palak Paneer", PortionType.FULL),
            TestCase("हाफ पालक पनीर", "Palak Paneer", PortionType.HALF),

            TestCase("full paneer butter masala", "Paneer butter Masala", PortionType.FULL),
            TestCase("half paneer butter masala", "Paneer butter Masala", PortionType.HALF),
            TestCase("फुल पनीर बटर मसाला", "Paneer butter Masala", PortionType.FULL),
            TestCase("हाफ पनीर बटर मसाला", "Paneer butter Masala", PortionType.HALF),

            // Breads & Roti
            TestCase("4 butter roti", "Tandoori Butter Roti", PortionType.FULL),
            TestCase("4 बटर रोटी", "Tandoori Butter Roti", PortionType.FULL),
            TestCase("1 lachha paratha", "Lachha Pararha", PortionType.FULL),
            TestCase("1 लच्छा पराठा", "Lachha Pararha", PortionType.FULL),
            TestCase("1 butter naan", "Butter Naan", PortionType.FULL),
            TestCase("1 बटर नान", "Butter Naan", PortionType.FULL),
            TestCase("1 garlic naan", "Garlic Naan", PortionType.FULL),
            TestCase("1 गार्लिक नान", "Garlic Naan", PortionType.FULL)
        )

        val failedCases = mutableListOf<String>()

        for (tc in testCases) {
            val result = parser.parseSpokenCommand(
                spokenText = tc.input,
                allItems = fullMenuItems
            )

            val matchedItem = result.firstOrNull()
            if (matchedItem == null) {
                failedCases.add("Input '${tc.input}' produced NO MATCH (expected '${tc.expectedItemName}')")
            } else if (matchedItem.menuItem.name != tc.expectedItemName) {
                failedCases.add("Input '${tc.input}' expected '${tc.expectedItemName}' but got '${matchedItem.menuItem.name}'")
            } else if (matchedItem.portion != tc.expectedPortion) {
                failedCases.add("Input '${tc.input}' expected portion '${tc.expectedPortion}' but got '${matchedItem.portion}'")
            }
        }

        assertEquals(emptyList(), failedCases, "Failed matching cases found:\n${failedCases.joinToString("\n")}")
    }
}
