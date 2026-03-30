
package com.sample.restaurantordertakingapp.domain.usecase.order


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


class PlaceOrderUseCaseTest {

    @MockK
    lateinit var cartRepository: CartRepository
    @MockK
    lateinit var orderRepository: OrderRepository
    @MockK
    lateinit var orderSyncRepository: OrderSyncRepository
    @MockK
    lateinit var notificationHelper: NotificationHelper

    private lateinit var placeOrderUseCase: PlaceOrderUseCase

    // ── Fake Data — tumhare actual models ke hisaab se ──────
    private val fakeAddress = Address(
        society = "Green Park Society",
        flatNo = "B-204",
        tower = "Tower B",
        mobile = "9876543210",
        orderId = ""
    )

    private val fakeDineInCartItem = CartItem(
        id = 1,
        menuItemId = 101,
        imageUrl = null,
        quantity = 2,
        portion = PortionType.FULL,
        halfPrice = 150,
        fullPrice = 250,
        tableId = "T5",
        name = "Paneer Butter Masala"
    )
    // unitPrice = 250 (FULL), totalPrice = 500

    private val fakeTakeawayCartItem = CartItem(
        id = 2,
        menuItemId = 102,
        imageUrl = null,
        quantity = 1,
        portion = PortionType.HALF,
        halfPrice = 120,
        fullPrice = 200,
        tableId = null,   // ← takeaway
        name = "Dal Makhani"
    )
    // unitPrice = 120 (HALF), totalPrice = 120

    private val fakeOrder = Order(
        id = "uuid-order-001",
        orderNumber = 5,
        orderDate = "2024-01-15",
        totalAmount = 500,
        status = OrderStatus.CREATED,
        createdAt = 1705305600L,
        items = emptyList()
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        placeOrderUseCase = PlaceOrderUseCase(
            cartRepository,
            orderRepository,
            orderSyncRepository,
            notificationHelper
        )
    }

    // ════════════════════════════════════════════════════════
    // ✅ HAPPY PATH
    // ════════════════════════════════════════════════════════
    @Nested
    inner class HappyPath {

        @Test
        fun invoke_dineInItem_orderTypeIsDineInAndTableNoSet() = runTest {
            // Arrange
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            // Act
            placeOrderUseCase(fakeAddress)

            // Assert — tableId "T5" hona chahiye, orderType "T5"
            coVerify {
                orderRepository.createOrder(
                    items = match { items ->
                        items.first().orderType == "T5" &&
                                items.first().tableNo == "T5" &&
                                items.first().isFull == true
                    },
                    totalAmount = 500,   // 250 * 2
                    address = fakeAddress
                )
            }
        }

        @Test
        fun invoke_takeawayItem_orderTypeIsTakeawayAndTableNoIsNull() = runTest {
            // Arrange — tableId null hai cart item mein
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeTakeawayCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            // Act
            placeOrderUseCase(fakeAddress)

            // Assert — "takeaway" aana chahiye
            coVerify {
                orderRepository.createOrder(
                    items = match { items ->
                        items.first().orderType == "takeaway" &&
                                items.first().tableNo == null && !items.first().isFull  // HALF portion
                    },
                    totalAmount = 120,   // 120 * 1
                    address = fakeAddress
                )
            }
        }

        @Test
        fun invoke_halfPortionItem_unitPriceIsHalfPrice() = runTest {
            // Arrange — HALF portion: unitPrice = halfPrice = 120
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeTakeawayCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            // Act
            placeOrderUseCase(fakeAddress)

            // Assert — price 120 hona chahiye (half price)
            coVerify {
                orderRepository.createOrder(
                    items = match { items -> items.first().price == 120 },
                    totalAmount = any(),
                    address = any()
                )
            }
        }

        @Test
        fun invoke_fullPortionItem_unitPriceIsFullPrice() = runTest {
            // Arrange — FULL portion: unitPrice = fullPrice = 250
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            // Act
            placeOrderUseCase(fakeAddress)

            // Assert — price 250 hona chahiye (full price)
            coVerify {
                orderRepository.createOrder(
                    items = match { items -> items.first().price == 250 },
                    totalAmount = any(),
                    address = any()
                )
            }
        }

        @Test
        fun invoke_multipleItems_totalIsCorrectSum() = runTest {
            // Arrange — item1: 500 + item2: 120 = 620
            coEvery { cartRepository.getAllCartItems() } returns
                    listOf(fakeDineInCartItem, fakeTakeawayCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            // Act
            placeOrderUseCase(fakeAddress)

            // Assert — total 620 hona chahiye
            coVerify {
                orderRepository.createOrder(
                    items = any(),
                    totalAmount = 620,
                    address = fakeAddress
                )
            }
        }

        @Test
        fun invoke_success_firebaseSyncCalledWithCorrectOrderId() = runTest {
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            placeOrderUseCase(fakeAddress)

            // Firebase ko sahi order ID bhejna chahiye
            coVerify(exactly = 1) {
                orderSyncRepository.syncSingleOrder("uuid-order-001")
            }
        }

        @Test
        fun invoke_success_notificationShownWithCorrectOrderNumber() = runTest {
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            placeOrderUseCase(fakeAddress)

            // orderNumber = 5 (fakeOrder mein)
            verify(exactly = 1) {
                notificationHelper.showNewOrderNotification(5)
            }
        }

        @Test
        fun invoke_success_cartClearedAtEnd() = runTest {
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            placeOrderUseCase(fakeAddress)

            coVerify(exactly = 1) { cartRepository.clearCart() }
        }
    }

    @Nested
    inner class EdgeCases {

        @Test
        fun invoke_emptyCart_throwsIllegalStateException() = runTest {
            coEvery { cartRepository.getAllCartItems() } returns emptyList()

            val exception = assertThrows<IllegalStateException> {
                placeOrderUseCase(fakeAddress)
            }

            assertEquals("Cart is empty", exception.message)

            // Kuch bhi nahi hona chahiye aage
            coVerify(exactly = 0) { orderRepository.createOrder(any(), any(), any()) }
            coVerify(exactly = 0) { orderSyncRepository.syncSingleOrder(any()) }
            verify(exactly = 0)   { notificationHelper.showNewOrderNotification(any()) }
            coVerify(exactly = 0) { cartRepository.clearCart() }
        }

        @Test
        fun invoke_quantity1_totalEqualToUnitPrice() = runTest {
            // quantity = 1, fullPrice = 250 → totalPrice = 250
            val singleQtyItem = fakeDineInCartItem.copy(quantity = 1)
            coEvery { cartRepository.getAllCartItems() } returns listOf(singleQtyItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every  { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            placeOrderUseCase(fakeAddress)

            coVerify {
                orderRepository.createOrder(
                    items       = any(),
                    totalAmount = 250,
                    address     = any()
                )
            }
        }

        @Test
        fun invoke_addressWithNullFields_orderStillCreated() = runTest {
            // society, flatNo, tower — sab null ho sakta hai
            val minimalAddress = Address(
                society  = null,
                flatNo   = null,
                tower    = null,
                mobile   = "9876543210",
                orderId  = ""
            )
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery { orderSyncRepository.syncSingleOrder(any()) } just Runs
            every  { notificationHelper.showNewOrderNotification(any()) } just Runs
            coEvery { cartRepository.clearCart() } just Runs

            // Null address fields ke saath bhi crash nahi hona chahiye
            placeOrderUseCase(minimalAddress)

            coVerify(exactly = 1) { orderRepository.createOrder(any(), any(), minimalAddress) }
        }
    }

    // ════════════════════════════════════════════════════════
    // 💥 ERROR CASES
    // ════════════════════════════════════════════════════════
    @Nested
    inner class ErrorCases {

        @Test
        fun invoke_orderRepositoryThrows_cartNotCleared() = runTest {
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery {
                orderRepository.createOrder(any(), any(), any())
            } throws RuntimeException("Server error")

            assertThrows<RuntimeException> {
                placeOrderUseCase(fakeAddress)
            }

            // Cart clear NAHI honi chahiye — order hi nahi bana!
            coVerify(exactly = 0) { cartRepository.clearCart() }
            coVerify(exactly = 0) { orderSyncRepository.syncSingleOrder(any()) }
            verify(exactly = 0)   { notificationHelper.showNewOrderNotification(any()) }
        }

        @Test
        fun invoke_syncRepositoryThrows_cartNotCleared() = runTest {
            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } returns fakeOrder
            coEvery {
                orderSyncRepository.syncSingleOrder(any())
            } throws RuntimeException("Firebase down")

            assertThrows<RuntimeException> {
                placeOrderUseCase(fakeAddress)
            }

            coVerify(exactly = 0) { cartRepository.clearCart() }
            verify(exactly = 0)   { notificationHelper.showNewOrderNotification(any()) }
        }

        @Test
        fun invoke_cartRepositoryThrows_nothingElseExecutes() = runTest {
            coEvery {
                cartRepository.getAllCartItems()
            } throws RuntimeException("DB crashed")

            assertThrows<RuntimeException> {
                placeOrderUseCase(fakeAddress)
            }

            coVerify(exactly = 0) { orderRepository.createOrder(any(), any(), any()) }
        }
    }

    // ════════════════════════════════════════════════════════
    // 🔄 EXECUTION SEQUENCE TEST
    // ════════════════════════════════════════════════════════
    @Nested
    inner class ExecutionSequence {

        @Test
        fun invoke_success_stepsExecuteInCorrectOrder() = runTest {
            val callOrder = mutableListOf<String>()

            coEvery { cartRepository.getAllCartItems() } returns listOf(fakeDineInCartItem)
            coEvery { orderRepository.createOrder(any(), any(), any()) } coAnswers {
                callOrder.add("createOrder")
                fakeOrder
            }
            coEvery { orderSyncRepository.syncSingleOrder(any()) } coAnswers {
                callOrder.add("syncOrder")
            }
            every { notificationHelper.showNewOrderNotification(any()) } answers {
                callOrder.add("notification")
            }
            coEvery { cartRepository.clearCart() } coAnswers {
                callOrder.add("clearCart")
            }

            placeOrderUseCase(fakeAddress)

            // Yeh sequence sahi honi chahiye — exactly as code mein likha hai
            assertEquals(
                listOf("createOrder", "syncOrder", "notification", "clearCart"),
                callOrder
            )
        }
    }
}

