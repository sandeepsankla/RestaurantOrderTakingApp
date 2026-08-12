package com.sample.restaurantordertakingapp.ui.theme.screen.order

/**
 * Order items ko station ke hisaab se baanto:
 *  - TANDOOR: Tandoori Snacks (chaap/tikka), Breads (roti/naan/paratha), Tandoori Momos, Thali (roti part)
 *  - KITCHEN: baaki sab
 *
 * Note: order item me sirf NAAM hota hai (category nahi), isliye classification naam ke
 * keyword se hoti hai. Gravy/rice/roll waale chaap kitchen me hi rehte hain (override).
 * 100% accurate chahiye to order item pe category tag store karna hoga (bada change).
 */
enum class Station(val label: String) { TANDOOR("Tandoor"), KITCHEN("Kitchen") }

private val TANDOOR_KEYS = Regex("chaap|chap|tikka|roti|naan|paratha|pararha|tandoori momo", RegexOption.IGNORE_CASE)
// Chaap/tikka jo actually gravy/kitchen ke hain (dry tandoori-snack chaap tandoor me rehta hai)
private val KITCHEN_OVERRIDE = Regex("gravy|kadai|twa|shahi|lababdar|butter masala|briyani|biryani|roll|rice|pulav|manchurian|noodle|chowmein", RegexOption.IGNORE_CASE)

fun stationsFor(itemName: String): Set<Station> {
    if (itemName.contains("thali", ignoreCase = true)) {
        return setOf(Station.TANDOOR, Station.KITCHEN) // thali: roti->tandoor, sabzi->kitchen
    }
    val tandoor = TANDOOR_KEYS.containsMatchIn(itemName) && !KITCHEN_OVERRIDE.containsMatchIn(itemName)
    return if (tandoor) setOf(Station.TANDOOR) else setOf(Station.KITCHEN)
}

/** Us station ke liye order ki filtered copy (sirf relevant items). Koi item na ho to null. */
fun OrderUi.forStation(station: Station): OrderUi? {
    val lines = items.filter { station in stationsFor(it.name) }
    if (lines.isEmpty()) return null
    return copy(
        items = lines,
        itemsText = lines.joinToString("\n") { "${it.name} x${it.quantity}" }
    )
}
