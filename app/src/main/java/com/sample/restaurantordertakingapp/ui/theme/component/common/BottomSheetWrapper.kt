package com.sample.restaurantordertakingapp.ui.theme.component.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun BottomSheetWrapper(
        showSheet: Boolean,
        onDismiss: () -> Unit,
        content: @Composable () -> Unit
    ) {
    val sheetState = rememberModalBottomSheetState()

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            content()
        }
    }
}
