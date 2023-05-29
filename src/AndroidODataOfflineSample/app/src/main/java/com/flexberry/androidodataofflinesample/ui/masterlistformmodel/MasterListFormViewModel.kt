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
class MasterListFormViewModel @Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator
) : ViewModel() {
    val masters = mutableStateOf(getMasters(applicationState.isOnline.value))

    init {
        // Пример слежки за изменением онлайна.
        snapshotFlow { applicationState.isOnline.value }.onEach {
            refreshData()
        }
        .launchIn(viewModelScope)
    }

    /**
     * Событие добавления мастера.
     */
    fun onAddMasterButtonClicked():Unit {
        appNavigator.tryNavigateTo(
            Destination.MasterEditScreen(
                primaryKey = ""
            )
        )
    }

    /**
     * Редактирование мастера.
     */
    fun onEditMasterClicked(master: Master):Unit {
        appNavigator.tryNavigateTo(
            Destination.MasterEditScreen(
                primaryKey = master.primarykey.toString()
            )
        )
    }

    /**
     * Удаление мастера.
     */
    fun onDeleteMasterClicked(master: Master):Unit {
        if (applicationState.isOnline.value) {
            repository.deleteMastersOnline(listOf(master))
        } else {
            repository.deleteMastersOffline(listOf(master))
        }

        refreshData()
    }

    /**
     * Событие возврата.
     */
    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }

    /**
     * Получить мастеров из хранилища.
     */
    private fun getMasters(isOnline: Boolean): List<Master> {
        return if (isOnline) {
            repository.getMastersOnline(10)
        } else {
            repository.getMastersOffline(10)
        }
    }

    fun refreshData() {
        masters.value = getMasters(applicationState.isOnline.value)
    }
}