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

private data class CandidateMatch(
    val item: MenuItemUi,
    val startIndex: Int,
    val endIndex: Int,
    val score: Double,
    val matchedText: String
)

class VoiceOrderParser @Inject constructor() {

    fun parseSpokenCommand(
        spokenText: String,
        allItems: List<MenuItemUi>
    ): List<VoiceParsedItem> {
        if (spokenText.isBlank() || allItems.isEmpty()) return emptyList()

        val normalizedInput = expandSpokenAliases(normalizeText(spokenText))
        val candidates = mutableListOf<CandidateMatch>()

        val genericWords = listOf(
            "veg", "nonveg", "tandoori", "special", "plate", "gravy", "dry", "fry", "item", "fresh", "hot", "deluxe",
            "chilly", "chilli", "chili", "noodles", "noodle", "chowmein", "chowmin", "chowmeen", "chomin", "chomeen",
            "chaumin", "chaumeen", "rice", "momos", "momo", "burger", "roll", "roti", "naan", "paratha", "pararha", "chaap", "chap"
        )

        // 1. Scan all menu items against normalized input to find potential matches
        for (item in allItems) {
            val itemNorm = normalizeText(item.name)
            val itemTokens = itemNorm.split(" ").filter { it.length >= 3 }

            // Check full name match first
            val fullIdx = normalizedInput.indexOf(itemNorm)
            if (fullIdx != -1) {
                candidates.add(
                    CandidateMatch(
                        item = item,
                        startIndex = fullIdx,
                        endIndex = fullIdx + itemNorm.length,
                        score = 100.0 + itemNorm.length,
                        matchedText = itemNorm
                    )
                )
                continue
            }

            // Check multi-word phrase match (e.g. "chilly paneer", "fried rice", "butter roti", "veg noodles", "dal makhni")
            if (itemTokens.size >= 2) {
                for (i in 0 until itemTokens.size - 1) {
                    val phrase = "${itemTokens[i]} ${itemTokens[i + 1]}"
                    val phraseIdx = normalizedInput.indexOf(phrase)
                    if (phraseIdx != -1) {
                        candidates.add(
                            CandidateMatch(
                                item = item,
                                startIndex = phraseIdx,
                                endIndex = phraseIdx + phrase.length,
                                score = 60.0 + phrase.length,
                                matchedText = phrase
                            )
                        )
                        break
                    }
                }
                if (candidates.any { it.item.id == item.id }) continue
            }

            // Check single strong keyword match (e.g. "makhni", "paneer", "singapuri", "hakka", "malai", "achari")
            for (tok in itemTokens) {
                if (tok.length >= 3 && tok !in genericWords) {
                    val tokIdx = normalizedInput.indexOf(tok)
                    if (tokIdx != -1) {
                        candidates.add(
                            CandidateMatch(
                                item = item,
                                startIndex = tokIdx,
                                endIndex = tokIdx + tok.length,
                                score = 30.0 + tok.length,
                                matchedText = tok
                            )
                        )
                        break
                    }
                }
            }
        }

        // 2. Sort candidates by score descending and suppress overlapping weak matches
        val selectedMatches = mutableListOf<CandidateMatch>()
        val sortedCandidates = candidates.sortedByDescending { it.score }

        for (cand in sortedCandidates) {
            val overlaps = selectedMatches.any { sel ->
                maxOf(cand.startIndex, sel.startIndex) < minOf(cand.endIndex, sel.endIndex) ||
                sel.item.id == cand.item.id
            }

            if (!overlaps) {
                selectedMatches.add(cand)
            }
        }

        // 3. Sort selected matches by their appearance order in the spoken text
        selectedMatches.sortBy { it.startIndex }

        // 4. Extract Portion & Quantity for each selected match from immediate prefix context
        val result = mutableListOf<VoiceParsedItem>()
        for (match in selectedMatches) {
            val windowStart = (match.startIndex - 20).coerceAtLeast(0)
            val prefixContext = normalizedInput.substring(windowStart, match.startIndex)

            val portion = detectPortion(prefixContext)
            val quantity = parseQuantity(prefixContext)

            result.add(
                VoiceParsedItem(
                    menuItem = match.item,
                    quantity = quantity,
                    portion = portion
                )
            )
        }

        return result
    }

    private fun detectPortion(text: String): PortionType {
        return when {
            text.contains("half") ||
            text.contains("haaf") ||
            text.contains("haf") ||
            text.contains("हाफ") ||
            text.contains("हाफ़") -> PortionType.HALF
            else -> PortionType.FULL
        }
    }

    private fun parseQuantity(text: String): Int {
        val matches = Regex("\\b(\\d+)\\b").findAll(text).toList()
        if (matches.isNotEmpty()) {
            return matches.last().groupValues[1].toIntOrNull()?.coerceAtLeast(1) ?: 1
        }

        return when {
            text.contains("ek") || text.contains("एक") || text.contains("one") || text.contains("single") -> 1
            text.contains("do") || text.contains("दो") || text.contains("two") || text.contains("double") -> 2
            text.contains("teen") || text.contains("तीन") || text.contains("three") || text.contains("triple") -> 3
            text.contains("chaar") || text.contains("char") || text.contains("चार") || text.contains("four") -> 4
            text.contains("paanch") || text.contains("panch") || text.contains("पांच") || text.contains("five") -> 5
            text.contains("che") || text.contains("छह") || text.contains("six") -> 6
            else -> 1
        }
    }

    private fun normalizeText(input: String): String {
        var text = input.lowercase()

        // 1. Full Devanagari Hindi -> English Transliteration Dictionary
        val devanagariMap = mapOf(
            "मिक्स" to "mix",
            "वेज" to "veg",
            "नूडल्स" to "noodles",
            "सिंगापुरी" to "singapori",
            "हक्का" to "hakka",
            "गार्लिक" to "garlic",
            "स्टीम" to "steam",
            "फ्राइड" to "fried",
            "कुरकुरे" to "kurkure",
            "कुड़कुड़े" to "kurkure",
            "तंदूरी" to "tandoori",
            "अफगानी" to "afgani",
            "अचारी" to "achari",
            "पोटैटो" to "potato",
            "हनी" to "honey",
            "चिली" to "chilly",
            "फ्रेंच" to "french",
            "फ्राइज" to "fries",
            "मंचूरियन" to "manchurian",
            "कॉर्न" to "corn",
            "बर्गर" to "burger",
            "रोल" to "roll",
            "स्प्रिंग" to "spring",
            "काठी" to "kathi",
            "चीज़" to "cheese",
            "ग्रेवी" to "gravy",
            "ड्राई" to "dry",
            "तवा" to "twa",
            "दाल" to "dal",
            "तड़का" to "tadka",
            "तड़का" to "tadka",
            "मखनी" to "makhni",
            "मखणी" to "makhni",
            "शाही" to "shahi",
            "पनीर" to "paneer",
            "पैनर" to "paneer",
            "कड़ाई" to "kadai",
            "कढाई" to "kadai",
            "पढ़ाई" to "kadai",
            "बटर" to "butter",
            "मसाला" to "masala",
            "रोटी" to "roti",
            "नान" to "naan",
            "नाण" to "naan",
            "चौमीन" to "noodles",
            "चाउमीन" to "noodles",
            "मोमो" to "momos",
            "मोमोज" to "momos",
            "टिक्का" to "tikka",
            "मलाई" to "malai",
            "आलू" to "aloo",
            "अलू" to "aloo",
            "गोभी" to "gobhi",
            "गोबी" to "gobhi",
            "मटर" to "matar",
            "चना" to "chana",
            "राजमा" to "rajma",
            "पालक" to "palak",
            "जीरा" to "jeera",
            "पुलाव" to "pulav",
            "बिरयानी" to "briyani",
            "सोया" to "soya",
            "चाप" to "chap",
            "छाप" to "chap",
            "मशरूम" to "mushroom",
            "लबाबदार" to "lababdar",
            "भुर्जी" to "bhurji",
            "कस्तूरी" to "kasturi",
            "काजू" to "kaju",
            "बूंदी" to "boondi",
            "रायता" to "raita",
            "पीनट" to "peanut",
            "पिनट" to "peanut",
            "पापड़" to "papad",
            "सलाद" to "salad",
            "ग्रीन" to "green",
            "दम" to "dum",
            "कोफ्ता" to "kofta",
            "सेव" to "sev",
            "भाजी" to "bhaji",
            "थाली" to "thali",
            "मिस्सी" to "missi",
            "प्याज" to "pyaz",
            "रुमाली" to "rumali",
            "लच्छा" to "lachha",
            "पराठा" to "pararha",
            "राइस" to "rice",
            "चावल" to "chawal",
            "हाफ" to "half",
            "हाफ़" to "half",
            "फुल" to "full",
            "फूल" to "full",
            "प्लेट" to "plate",
            "एक" to "1",
            "दो" to "2",
            "तीन" to "3",
            "चार" to "4",
            "पांच" to "5",
            "छह" to "6"
        )

        for ((hindi, eng) in devanagariMap) {
            text = text.replace(hindi, eng)
        }

        // 2. English Phonetic & Synonym Normalization (All Chowmein/Chowmin/Chomin/Chowmeen variations)
        return text
            .replace("singapori", "singapuri")
            .replace("shezwan", "schezwan")
            .replace("chowmein", "noodles")
            .replace("chowmin", "noodles")
            .replace("chowmeen", "noodles")
            .replace("chomin", "noodles")
            .replace("chomeen", "noodles")
            .replace("chaumin", "noodles")
            .replace("chaumeen", "noodles")
            .replace("chaomin", "noodles")
            .replace("chaowmein", "noodles")
            .replace("chowman", "noodles")
            .replace("chawmin", "noodles")
            .replace("makhani", "makhni")
            .replace("makni", "makhni")
            .replace("panir", "paneer")
            .replace("panner", "paneer")
            .replace("haaf", "half")
            .replace("ful", "full")
            .replace("chaap", "chap")
            .replace("paratha", "pararha")
            .replace("biryani", "briyani")
            .replace("mushrom", "mushroom")
            .replace("kajui", "kaju")
            .trim()
    }

    private fun expandSpokenAliases(input: String): String {
        var text = input
        if (text.contains("fried rice") && !text.contains("veg fried rice") && !text.contains("paneer fried rice") && !text.contains("singapuri") && !text.contains("schezwan")) {
            text = text.replace("fried rice", "veg fried rice")
        }
        if ((text.contains("noodle") || text.contains("noodles")) &&
            !text.contains("veg noodles") && !text.contains("paneer butter") && !text.contains("hakka") && !text.contains("singapuri") && !text.contains("chilly garlic") && !text.contains("manchurian")) {
            text = text.replace("noodles", "veg noodles")
                .replace("noodle", "veg noodles")
        }
        if ((text.contains("momo") || text.contains("momos")) &&
            !text.contains("veg steam momos") && !text.contains("paneer") && !text.contains("kurkure") && !text.contains("fried") && !text.contains("tandoori")) {
            text = text.replace("momos", "veg steam momos")
                .replace("momo", "veg steam momos")
        }

        return text
    }
}
