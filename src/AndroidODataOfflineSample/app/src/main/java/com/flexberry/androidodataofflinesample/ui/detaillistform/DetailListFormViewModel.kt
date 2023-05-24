package com.flexberry.androidodataofflinesample.ui.detaillistform

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.DetailRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailListFormViewModel@Inject constructor(
    private val repository: DetailRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun onAddDetailButtonClicked():Unit {
        // добавление нового пользователя
    }

    fun onEditDetailClicked():Unit {
        // изменение данных пользователя
    }

    fun onDeleteDetailClicked():Unit {
        // удаление пользователя
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }
}