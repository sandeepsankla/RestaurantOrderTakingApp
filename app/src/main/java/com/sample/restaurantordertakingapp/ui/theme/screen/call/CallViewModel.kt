package com.sample.restaurantordertakingapp.ui.theme.screen.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.restaurantordertakingapp.domain.model.CallSignal
import com.sample.restaurantordertakingapp.domain.repo.CallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val repo: CallRepository
) : ViewModel() {

    val calls: Flow<CallSignal> = repo.observeCalls()

    fun send(message: String) {
        viewModelScope.launch {
            runCatching { repo.sendCall(message) }
        }
    }
}
