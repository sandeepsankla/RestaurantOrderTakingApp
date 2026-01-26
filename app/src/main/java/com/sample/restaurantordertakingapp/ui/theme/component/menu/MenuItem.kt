package com.sample.restaurantordertakingapp.ui.theme.component.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import com.sample.restaurantordertakingapp.utils.NetworkImage

@Composable
fun MenuItemCard(menuItem: MenuItemUi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Placeholder image
            NetworkImage(menuItem.imageUrl, menuItem.name, modifier = Modifier.fillMaxWidth().height(80.dp))
            Spacer(Modifier.height(8.dp))
            Text(menuItem.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(menuItem.getFormattedPrice(), style = MaterialTheme.typography.headlineSmall)

        }
    }
}