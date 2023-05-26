package com.flexberry.androidodataofflinesample.ui.mastereditformmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.MasterRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.ui.detaillistform.SampleDetailData
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MasterEditFormViewModel@Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator
):ViewModel() {
    private val _master = mutableStateOf(SampleDetailData.superMaster)
    val master = _master.value.copy()

    fun onCloseButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MasterListForm())
    }

    fun onSaveButtonClicked() {
        _master.value.name = master.name
    }
}