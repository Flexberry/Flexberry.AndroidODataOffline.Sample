package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationUserListFormViewModel @Inject constructor(
    private val repository: ApplicationUserRepository
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