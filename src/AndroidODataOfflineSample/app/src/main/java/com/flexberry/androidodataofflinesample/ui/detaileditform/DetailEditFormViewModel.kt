package com.flexberry.androidodataofflinesample.ui.detaileditform

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.DetailRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.data.model.Detail
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailEditFormViewModel@Inject constructor(
    private val repository: DetailRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var detailName = mutableStateOf("")
    var masterName = mutableStateOf("")
    var detail: Detail = Detail(UUID.randomUUID(), "", Master(UUID.randomUUID(),""))

    init {
        val primaryKey =
            savedStateHandle.get<String>(Destination.DetailEditScreen.DETAIL_PRIMARY_KEY) ?: ""

        detail = getDetailByPrimaryKey(primaryKey)
        detailName.value = detail.name ?: ""
        masterName.value = detail.master.name ?: ""
    }

    fun getDetailByPrimaryKey(primaryKey: String): Detail {
        // Изменить возвращаемое значение return на получение данных detail по primaryKey
        return detail
    }

    fun onCloseDetailClicked() {
        appNavigator.tryNavigateBack(Destination.DetailListForm())
    }

    fun onSaveDetailClicked() {
        // сохранение изменного Мастера
        detail.name = detailName.value
    }

    fun onSaveCloseDetailClicked() {
        onSaveDetailClicked()
        onCloseDetailClicked()
    }
}