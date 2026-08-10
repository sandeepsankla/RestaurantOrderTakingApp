package com.sample.restaurantordertakingapp.domain.usecase.cart

import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.domain.model.PortionType
import com.sample.restaurantordertakingapp.domain.pricing.CartPricing
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalculateCartSummaryUseCaseTest {

    private lateinit var useCase: CalculateCartSummaryUseCase

    @BeforeEach
    fun setUp() {
        useCase = CalculateCartSummaryUseCase()
    }

    // ── Helpers ────────────────────────────────────────────
    private fun item(
        id: Int = 1,
        quantity: Int = 1,
        portion: PortionType = PortionType.FULL,
        halfPrice: Int = 100,
        fullPrice: Int = 200,
    ) = CartItem(
        id = id,
        menuItemId = id,
        imageUrl = null,
        quantity = quantity,
        portion = portion,
        halfPrice = halfPrice,
        fullPrice = fullPrice,
        tableId = null,
        name = "Item $id",
    )

    /** Expected tax jo bhi current TAX_RATE ho — test rate-change pe bhi sahi rahe. */
    private fun expectedTax(subtotal: Int) = (subtotal * CartPricing.TAX_RATE).roundToInt()

    @Nested
    inner class EmptyCart {

        @Test
        fun emptyList_isEmptyTrue_andAllZero() {
            val summary = useCase(emptyList())

            assertTrue(summary.isEmpty)
            assertEquals(0.0, summary.subtotal)
            assertEquals(0.0, summary.tax)
            assertEquals(0.0, summary.total)
        }
    }

    @Nested
    inner class Subtotal {

        @Test
        fun fullPortion_usesFullPrice() {
            // FULL: 250 * 2 = 500
            val summary = useCase(listOf(item(quantity = 2, portion = PortionType.FULL, fullPrice = 250)))

            assertFalse(summary.isEmpty)
            assertEquals(500.0, summary.subtotal)
        }

        @Test
        fun halfPortion_usesHalfPrice() {
            // HALF: 120 * 3 = 360
            val summary = useCase(listOf(item(quantity = 3, portion = PortionType.HALF, halfPrice = 120)))

            assertEquals(360.0, summary.subtotal)
        }

        @Test
        fun multipleItems_subtotalIsSum() {
            // 250*2 (FULL) + 120*1 (HALF) = 620
            val items = listOf(
                item(id = 1, quantity = 2, portion = PortionType.FULL, fullPrice = 250),
                item(id = 2, quantity = 1, portion = PortionType.HALF, halfPrice = 120),
            )

            val summary = useCase(items)

            assertEquals(620.0, summary.subtotal)
        }

        @Test
        fun quantityIsMultiplied() {
            val summary = useCase(listOf(item(quantity = 5, portion = PortionType.FULL, fullPrice = 100)))

            assertEquals(500.0, summary.subtotal)
        }
    }

    @Nested
    inner class TaxAndTotal {

        @Test
        fun total_alwaysEqualsSubtotalPlusTax() {
            // Invariant — TAX_RATE kuch bhi ho, hamesha true rehna chahiye
            val summary = useCase(listOf(item(quantity = 2, fullPrice = 250)))

            assertEquals(summary.subtotal + summary.tax, summary.total)
        }

        @Test
        fun tax_matchesConfiguredRate() {
            val items = listOf(item(quantity = 2, fullPrice = 250)) // subtotal 500
            val summary = useCase(items)

            assertEquals(expectedTax(500).toDouble(), summary.tax)
            assertEquals((500 + expectedTax(500)).toDouble(), summary.total)
        }

        @Test
        fun emptyCart_taxIsZero() {
            val summary = useCase(emptyList())

            assertEquals(0.0, summary.tax)
        }
    }
}
