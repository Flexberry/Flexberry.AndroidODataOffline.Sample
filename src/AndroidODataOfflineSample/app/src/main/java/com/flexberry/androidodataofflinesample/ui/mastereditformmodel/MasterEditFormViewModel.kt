package com.flexberry.androidodataofflinesample.ui.mastereditformmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
class MasterEditFormViewModel @Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    /**
     * Изменяемое имя мастера.
     */
    var mutableName by mutableStateOf("")

    /**
     * Мастер.
     */
    var dataObject: Master = Master(UUID.randomUUID())

    init {
        val primaryKey =
            savedStateHandle.get<String>(Destination.MasterEditScreen.MASTER_PRIMARY_KEY) ?: ""

        if (primaryKey.isNotEmpty()) {
            dataObject = getMasterByPrimaryKey(UUID.fromString(primaryKey)) ?: dataObject
        }
        
        mutableName = dataObject.name ?: ""
    }

    /**
     * Получить мастера по ключу их хранилища.
     */
    fun getMasterByPrimaryKey(primaryKey: UUID): Master? {
        return if (applicationState.isOnline.value) {
            repository.getMasterByPrimaryKeyOnline(primaryKey)
        } else {
            repository.getMasterByPrimaryKeyOffline(primaryKey)
        }
    }

    /**
     * Событие закрытия формы.
     */
    fun onCloseMasterClicked() {
        appNavigator.tryNavigateBack(Destination.MasterListForm())
    }

    /**
     * Событие кнопки сохранения.
     */
    fun onSaveMasterClicked() {
        // сохранение изменного Мастера
        dataObject.name = mutableName
        
        if (applicationState.isOnline.value) {
            repository.updateMastersOnline(listOf(dataObject))
        } else {
            repository.updateMastersOffline(listOf(dataObject))
        }
    }

    fun onSaveCloseMasterClicked() {
        onSaveMasterClicked()
        onCloseMasterClicked()
    }
}