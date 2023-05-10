package com.flexberry.androidodataofflinesample.ui.mainmodel


class MainViewModel {
    data class MainViewModelState (val isOnline: Boolean)

    fun addButton():Unit {
        // Функционал для кнопки "+"
    }

    fun listButton():Unit {
        // Функционал для кнопки "Список"
    }

    fun onlineButton():Unit {
        // Функционал для кнопки "Offline"
    }
}