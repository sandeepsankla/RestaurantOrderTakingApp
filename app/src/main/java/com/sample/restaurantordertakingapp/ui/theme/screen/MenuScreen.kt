package com.sample.restaurantordertakingapp.ui.theme.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.restaurantordertakingapp.data.model.Category
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.network.Resource
import com.sample.restaurantordertakingapp.utils.NetworkImage


@Composable
fun MenuScreen(context: Context, viewModel: MenuViewModel = hiltViewModel()) {

    viewModel.loadMenu()
    val menuState by viewModel.menuState.collectAsState()
    when (menuState) {
        is Resource.Loading -> {
            // show skeleton / loading indicator
            //MenuSkeleton()
        }
        is Resource.Error -> {
            Text("Error: ${(menuState as Resource.Error).message}")
        }
        is Resource.Success -> {
            val items = (menuState as Resource.Success).data
            Log.d("sasa","$items")
            MenuTabScreen(modifier = Modifier.padding(12.dp), categories = items.categories, onMenuItemClick = {
                Log.d("sasa", "cclicked")
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            })
            // Suppose you also have category list stored or passed
            // For simplicity, group items by category name (if you stored category in MenuItem)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen(modifier: Modifier, categories : List<Category>, onMenuItemClick : (MenuItem) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            categories.forEachIndexed { i, cat ->
                Tab(
                    selected = (i == selectedTab),
                    onClick = { selectedTab = i },
                    text = { Text(cat.name, modifier = Modifier.padding(16.dp)) }
                )
            }
        }

        // The grid of items for the selected category
        val menuItems = if (categories.isNotEmpty()) categories[selectedTab].items else emptyList()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems.size) { index ->
                MenuItemCard(
                    menuItem = menuItems[index],
                    onClick = { onMenuItemClick(menuItems[index]) }
                )
            }
        }
    }
}
@Composable
fun MenuItemCard(menuItem: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
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
            Text("₹${menuItem.price}", style = MaterialTheme.typography.headlineSmall)

        }
    }
}
