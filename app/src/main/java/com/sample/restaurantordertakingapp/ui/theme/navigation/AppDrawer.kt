package com.sample.restaurantordertakingapp.ui.theme.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    currentRoute: String?,
    onOrdersClick: () -> Unit,
    onFulfillmentClick: () -> Unit,
    onReportClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp)
    ) {

        // 🔥 HEADER
        DrawerHeader()

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(8.dp))

        // 🔥 TOP MENU ITEMS
        DrawerItem(
            label = "Orders",
            icon = Icons.Default.ReceiptLong,
            selected = currentRoute == Screen.Orders.route,
            onClick = onOrdersClick
        )

        DrawerItem(
            label = "Serve & Payment",
            icon = Icons.Default.PointOfSale,
            selected = currentRoute == Screen.Fulfillment.route,
            onClick = onFulfillmentClick
        )

        DrawerItem(
            label = "Daily Report",
            icon = Icons.Default.Assessment,
            selected = currentRoute == Screen.Report.route,
            onClick = onReportClick
        )

        // 👇 pushes logout to bottom
        Spacer(modifier = Modifier.weight(1f))

        Divider()


        DrawerLogoutItem(
            onClick = onLogoutClick
        )
    }
}
