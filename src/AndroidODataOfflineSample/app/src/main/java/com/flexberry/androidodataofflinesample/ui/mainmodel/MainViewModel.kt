package com.flexberry.androidodataofflinesample.ui.mainmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.data.AppDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository
) : ViewModel() {
    data class MainViewModelState (val isOnline: Boolean)

    val state = MainViewModelState(isOnline = false)

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