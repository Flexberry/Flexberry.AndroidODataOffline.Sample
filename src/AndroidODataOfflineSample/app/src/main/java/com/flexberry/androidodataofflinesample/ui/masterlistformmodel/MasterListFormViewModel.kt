package com.flexberry.androidodataofflinesample.ui.masterlistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.MasterRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MasterListFormViewModel@Inject constructor(
    private val repository: MasterRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun onAddMasterButtonClicked():Unit {
        // добавление нового пользователя
    }

    fun onEditMasterClicked():Unit {
        // изменение данных пользователя
    }

    fun onDeleteMasterClicked():Unit {
        // удаление пользователя
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }
}