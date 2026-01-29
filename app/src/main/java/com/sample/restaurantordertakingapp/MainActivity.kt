package com.sample.restaurantordertakingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sample.restaurantordertakingapp.ui.theme.navigation.AppNavigation
import com.sample.restaurantordertakingapp.ui.theme.theme.RestaurantOrderTakingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantOrderTakingAppTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) {
                    //MenuScreen()
                    AppNavigation()
                //}
            }
        }
    }
}
/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTabScreen(modifier: Modifier) {
//fun MenuTabScreen(modifier: Modifier, categories : List<Category>, onMenuItemClick : (MenuItem) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
//            categories.forEachIndexed { i, cat ->
//                Tab(selected = i == selectedTab, onClick = { selectedTab = i }) {
//                    Text(cat.name, modifier = Modifier.padding(16.dp))
//                }
//            }
        }

        // Placeholder grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {

//            items(count = categories.size) {
//                categories.forEach { category ->
//                    MenuItemCardSkeleton(category.items)
//                }
//
//            }
        }
    }
}

@Composable
fun MenuItemCardSkeleton(menuItems: MenuItem) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.2f))
            )
            Text(menuItems.name)
        }

}

*/



