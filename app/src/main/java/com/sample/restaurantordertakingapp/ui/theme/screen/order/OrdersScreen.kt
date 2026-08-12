package com.sample.restaurantordertakingapp.ui.theme.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.ui.theme.component.common.LoadingView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun OrdersScreen(
    state: OrdersUiState,
    onRefresh: () -> Unit,
    onStationReady: (String, Station) -> Unit
) {
    var selectedStation by remember { mutableStateOf(Station.TANDOOR) }

    val stationOrders = remember(state.orders, selectedStation) {
        state.orders.mapNotNull { it.forStation(selectedStation) }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔀 Station filter: Tandoor | Kitchen
        TabRow(selectedTabIndex = selectedStation.ordinal) {
            Station.entries.forEachIndexed { index, station ->
                Tab(
                    selected = selectedStation == station,
                    onClick = { selectedStation = station },
                    text = { Text(station.label) }
                )
            }
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                when {
                    state.isLoading && state.orders.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingView(PaddingValues(16.dp))
                        }
                    }

                    state.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(state.error)
                        }
                    }

                    stationOrders.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No ${selectedStation.label} orders",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = stationOrders, key = { it.orderId }) { order ->
                                OrderCard(
                                    order = order,
                                    station = selectedStation,
                                    onStationReady = onStationReady
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
