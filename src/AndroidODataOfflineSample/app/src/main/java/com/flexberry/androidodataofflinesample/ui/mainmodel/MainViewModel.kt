package com.flexberry.androidodataofflinesample.ui.mainmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.AppDataRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository,
    @AppState val applicationState: ApplicationState
) : ViewModel() {
    val btnText: MutableLiveData<String> = MutableLiveData("Offline")

    init {
        applicationState.isOnline.observeForever { newValue ->
            btnText.postValue(
                if (newValue) {
                    "Online"
                } else {
                    "Offline"
                }
            )
        }
    }

    fun appUserButton():Unit {
        // Функционал для кнопки "ApplicationUser"
    }

    fun voteButton():Unit {
        // Функционал для кнопки "Vote"
    }

    fun offlineButton():Unit {
        applicationState.setOnline(applicationState.isOnline.value == false)
    }
}