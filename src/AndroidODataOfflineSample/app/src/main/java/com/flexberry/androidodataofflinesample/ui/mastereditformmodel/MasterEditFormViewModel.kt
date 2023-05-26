package com.flexberry.androidodataofflinesample.ui.mastereditformmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.MasterRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.data.model.Detail
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.ui.detaillistform.SampleDetailData
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MasterEditFormViewModel@Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    var master: Master = Master(UUID.randomUUID(),"", emptyList())
    private var _master = mutableStateOf(master)

    private val _viewState = MutableStateFlow(MasterDetailsViewState())
    val viewState = _viewState.asStateFlow()
    init {
        val primaryKey =
            savedStateHandle.get<String>(Destination.MasterEditScreen.MASTER_PRIMARY_KEY) ?: ""

        _viewState.update {
            it.copy(
                primaryKey = primaryKey
            )
        }
        _master = mutableStateOf(getMasterByPrimaryKey(primaryKey))
        master = _master.value.copy()
    }

    fun getMasterByPrimaryKey(primaryKey: String): Master {
        var master = Master(UUID.fromString("4e0793de-fb8e-11ed-be56-0242ac120002"), "Alex", details = emptyList())
        val detailsSample = listOf(
            Detail(
                primarykey = UUID.randomUUID(),
                name = "Alex",
                master = SampleDetailData.master
            ),
            Detail(
                primarykey = UUID.randomUUID(),
                name = "Peter",
                master = SampleDetailData.master
            )
        )
        return Master(master.primarykey, master.name, details = detailsSample)
    }

    fun onCloseButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MasterListForm())
    }

    fun onSaveButtonClicked() {
        _master.value.name = master.name
    }
}