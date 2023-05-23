package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationUserListFormViewModel @Inject constructor(
    private val repository: ApplicationUserRepository,
    @AppState private val applicationState: ApplicationState
) : ViewModel() {

    fun addUser():Unit {
        // добавление нового пользователя
    }

    fun editUser():Unit {
        // изменение данных пользователя
    }

    fun deleteUser():Unit {
        // удаление пользователя
    }
}