package com.flexberry.androidodataofflinesample.ui.mainmodel

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.AppDataRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository,
    @AppState val applicationState: ApplicationState
    private val repository: AppDataRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {
    init {
        repository.initSettings()

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
    fun onApplicationUserButtonClicked():Unit {
        appNavigator.tryNavigateTo(Destination.ApplicationUserListFormModelScreen())
    }

    fun onVoteButtonClicked():Unit {
        appNavigator.tryNavigateTo(Destination.VoteListFormModelScreen())
    }

    fun offlineButton():Unit {
        val newValue = !applicationState.isOnline.value;

        if (repository.setOnlineFlag(newValue)) {
            applicationState.setOnline(newValue)
        }
    fun onOfflineButtonClicked():Unit {
        // Функционал для кнопки "Offline"
    }
}