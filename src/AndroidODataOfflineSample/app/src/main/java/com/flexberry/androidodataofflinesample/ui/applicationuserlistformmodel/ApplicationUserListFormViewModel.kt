package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.OnlineSwithcer
import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import com.flexberry.androidodataofflinesample.data.di.ApplicationOnlineSwithcer
import com.flexberry.androidodataofflinesample.ui.ListFromViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationUserListFormViewModel @Inject constructor(
    private val repository: ApplicationUserRepository,
    @ApplicationOnlineSwithcer private val onlineSwithcer: OnlineSwithcer
) : ListFromViewModel(false, onlineSwithcer) {

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