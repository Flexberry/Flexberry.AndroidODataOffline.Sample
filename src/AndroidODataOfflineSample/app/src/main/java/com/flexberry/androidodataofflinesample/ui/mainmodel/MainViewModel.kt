package com.flexberry.androidodataofflinesample.ui.mainmodel

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.AppDataRepository
import com.flexberry.androidodataofflinesample.data.TestDataRoomRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.data.synchonization.SynchronizationService
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository,
    private val testDataRoomRepository: TestDataRoomRepository,
    @AppState val applicationState: ApplicationState,
    private val appNavigator: AppNavigator,
    private val synchronizationService: SynchronizationService
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

    fun onMasterButtonClicked():Unit {
        appNavigator.tryNavigateTo(Destination.MasterListForm())
    }

    fun onDetailButtonClicked():Unit {
        appNavigator.tryNavigateTo(Destination.DetailListForm())
    }

    fun onOfflineButtonClicked():Unit {
        val isOnlineNewValue = !applicationState.isOnline.value;

        if (repository.setOnlineFlag(isOnlineNewValue)) {
            if (!isOnlineNewValue) {
                synchronizationService.sendRemoteDataToLocal()
            }

            applicationState.setOnline(isOnlineNewValue)
        }
    }

    fun onInitTestData() {
        testDataRoomRepository.initTestOfflineData()
    }
}