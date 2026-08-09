package com.sample.restaurantordertakingapp.domain.usecase.cart

import com.sample.restaurantordertakingapp.domain.model.*
import com.sample.restaurantordertakingapp.domain.repo.*
import com.sample.restaurantordertakingapp.utils.NotificationHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CalculateCartSummaryUseCaseTest {

    private lateinit var useCase: CalculateCartSummaryUseCase

    @BeforeEach
    fun setUp() {
        useCase = CalculateCartSummaryUseCase()
    }

    @Test
    fun invoke_emptyCart_returnsEmptySummaryWithZeroSubtotal() {
        val summary = useCase(emptyList())

        assertEquals(0.0, summary.subtotal)
        assertEquals(1.0, summary.tax)
        assertEquals(1.0, summary.total)
        assertEquals(true, summary.isEmpty)
    }

    @Test
    fun invoke_fullPortionItem_calculatesSubtotalUsingFullPrice() {
        val items = listOf(
            CartItem(
                id = 1,
                menuItemId = 101,
                name = "Paneer Butter Masala",
                imageUrl = null,
                quantity = 2,
                portion = PortionType.FULL,
                halfPrice = 150,
                fullPrice = 250,
                tableId = "T1"
            )
        )

        val summary = useCase(items)

        assertEquals(500.0, summary.subtotal) // 250 * 2
        assertEquals(1.0, summary.tax)
        assertEquals(501.0, summary.total)
        assertEquals(false, summary.isEmpty)
    }

    @Test
    fun invoke_halfPortionItem_calculatesSubtotalUsingHalfPrice() {
        val items = listOf(
            CartItem(
                id = 2,
                menuItemId = 102,
                name = "Dal Makhani",
                imageUrl = null,
                quantity = 3,
                portion = PortionType.HALF,
                halfPrice = 120,
                fullPrice = 200,
                tableId = null
            )
        )

        val summary = useCase(items)

        assertEquals(360.0, summary.subtotal) // 120 * 3
        assertEquals(1.0, summary.tax)
        assertEquals(361.0, summary.total)
        assertEquals(false, summary.isEmpty)
    }

    @Test
    fun invoke_multipleItems_calculatesCorrectTotalSum() {
        val items = listOf(
            CartItem(
                id = 1,
                menuItemId = 101,
                name = "Paneer Butter Masala",
                imageUrl = null,
                quantity = 2,
                portion = PortionType.FULL,
                halfPrice = 150,
                fullPrice = 250,
                tableId = "T1"
            ),
            CartItem(
                id = 2,
                menuItemId = 102,
                name = "Dal Makhani",
                imageUrl = null,
                quantity = 1,
                portion = PortionType.HALF,
                halfPrice = 120,
                fullPrice = 200,
                tableId = null
            )
        )

        val summary = useCase(items)

        // Subtotal = (250 * 2) + (120 * 1) = 500 + 120 = 620
        assertEquals(620.0, summary.subtotal)
        assertEquals(1.0, summary.tax)
        assertEquals(621.0, summary.total)
        assertEquals(false, summary.isEmpty)
    }
}