package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationUserListFormViewModel @Inject constructor(
    private val repository: ApplicationUserRepository,
    @AppState private val applicationState: ApplicationState
    private val repository: ApplicationUserRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun onAddUserButtonClicked():Unit {
        // добавление нового пользователя
    }

    fun onEditUserClicked():Unit {
        // изменение данных пользователя
    }

    fun onDeleteUserClicked():Unit {
        // удаление пользователя
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }
}