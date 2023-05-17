package com.flexberry.androidodataofflinesample.ui.mainmodel


class MainViewModel {
    data class MainViewModelState (val isOnline: Boolean)

    fun appUserButton():Unit {
        // Функционал для кнопки "ApplicationUser"
    }

    fun voteButton():Unit {
        // Функционал для кнопки "Vote"
    }

    fun offlineButton():Unit {
        // Функционал для кнопки "Offline"
    }
}