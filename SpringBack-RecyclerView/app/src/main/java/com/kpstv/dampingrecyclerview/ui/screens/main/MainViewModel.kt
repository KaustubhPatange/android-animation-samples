package com.kpstv.dampingrecyclerview.ui.screens.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val setElevatedToolbar = MutableStateFlow(false)
    val showElevatedToolbar: StateFlow<Boolean> = setElevatedToolbar

    fun setElevatedToolbar(enabled: Boolean) {
        this.setElevatedToolbar.value = enabled
    }
}