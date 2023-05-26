package com.flexberry.androidodataofflinesample.ui.masterlistformmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.MasterRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MasterListFormViewModel@Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator
) : ViewModel() {

    val masters = mutableStateOf(getMasters(applicationState.isOnline.value))

    init {
        repository.initTestOfflineData()

        // Пример слежки за изменением онлайна.
        snapshotFlow { applicationState.isOnline.value }.onEach { isOnline ->
            masters.value = getMasters(isOnline)
        }
        .launchIn(viewModelScope)
    }

    fun onAddMasterButtonClicked():Unit {
        // добавление нового пользователя
    }

    fun onEditMasterClicked(master: Master):Unit {
        appNavigator.tryNavigateTo(
            Destination.MasterEditScreen(
                primaryKey = master.primarykey.toString()
            )
        )
    }

    fun onDeleteMasterClicked():Unit {
        // удаление пользователя
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }

    private fun getMasters(isOnline: Boolean): List<Master> {
        return if (isOnline) {
            repository.getMastersOnline().take(10)
        } else {
            repository.getMastersOffline().take(10)
        }
    }
}