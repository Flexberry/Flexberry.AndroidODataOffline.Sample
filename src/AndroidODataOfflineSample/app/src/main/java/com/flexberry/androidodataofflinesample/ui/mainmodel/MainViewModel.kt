package com.flexberry.androidodataofflinesample.ui.mainmodel

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.AppDataRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository,
    @AppState val applicationState: ApplicationState
) : ViewModel() {
    init {
        // Пример слежки за изменением онлайна.
        snapshotFlow { applicationState.isOnline.value }
            .onEach { isOnline ->
                val state = if (isOnline) { "Online" } else { "Offline" }
                Log.d("Application state", "Application state changed to: $state")
            }
            .launchIn(viewModelScope)
    }

    fun appUserButton():Unit {
        // Функционал для кнопки "ApplicationUser"
    }

    fun voteButton():Unit {
        // Функционал для кнопки "Vote"
    }

    fun offlineButton():Unit {
        applicationState.setOnline(!applicationState.isOnline.value)
    }
}