package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel

import java.time.LocalDate

class ApplicationUserListFormViewModel {
    data class MainViewModelState (val isOnline: Boolean)

    data class User(
        val name: String,
        val email: String,
        val phone: String,
        val activated: Boolean = false,
        val vK: String = "",
        val facebook: String = "",
        val birthday: LocalDate,
    )

    fun editUser():Unit {
        // TODO изменение данных пользователя
    }

    fun deleteUser():Unit {
        // TODO удаление пользователя
    }
}