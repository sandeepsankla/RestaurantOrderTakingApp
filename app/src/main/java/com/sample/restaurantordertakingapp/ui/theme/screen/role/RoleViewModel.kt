package com.sample.restaurantordertakingapp.ui.theme.screen.role

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sample.restaurantordertakingapp.data.local.pref.AppMode
import com.sample.restaurantordertakingapp.data.local.pref.RoleStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val store: RoleStore
) : ViewModel() {

    var mode by mutableStateOf(store.getMode())
        private set

    val isPinSet: Boolean get() = store.isPinSet()

    /** First-time: role + PIN save karo. */
    fun setup(newMode: AppMode, pin: String) {
        store.setup(newMode, pin)
        mode = newMode
    }

    /** PIN verify hone ke baad mode switch. */
    fun switchTo(newMode: AppMode) {
        store.setMode(newMode)
        mode = newMode
    }

    fun checkPin(pin: String): Boolean = store.getPin() == pin
}
