package com.sample.restaurantordertakingapp.ui.theme.screen

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.restaurantordertakingapp.network.Resource
import kotlin.math.log


@Composable
fun MenuScreen(viewModel: MenuViewModel = hiltViewModel()) {

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
            // Suppose you also have category list stored or passed
            // For simplicity, group items by category name (if you stored category in MenuItem)

        }
    }
}

