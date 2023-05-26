package com.flexberry.androidodataofflinesample.ui.mastereditformmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.MasterRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MasterEditFormViewModel@Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    var masterName = mutableStateOf("")
    var master: Master = Master(UUID.randomUUID(),"")

    init {
        val primaryKey =
            savedStateHandle.get<String>(Destination.MasterEditScreen.MASTER_PRIMARY_KEY) ?: ""

        master = getMasterByPrimaryKey(primaryKey)
        masterName.value = master.name ?: ""
    }

    fun getMasterByPrimaryKey(primaryKey: String): Master {
        // Изменить возвращаемое значение return на получение данных мастера по primaryKey
        return master
    }

    fun onCloseButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MasterListForm())
    }

    fun onSaveButtonClicked() {
        // сохранение изменного Мастера
        master.name = masterName.value
    }
}