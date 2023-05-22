package com.flexberry.androidodataofflinesample.ui.mainmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.data.AppDataRepository
import com.flexberry.androidodataofflinesample.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {
    data class MainViewModelState (val isOnline: Boolean)

    fun onApplicationUserButtonClicked():Unit {
        appNavigator.tryNavigateTo(Destination.ApplicationUserListFormModelScreen())
    }

    fun onVoteButtonClicked():Unit {
        appNavigator.tryNavigateTo(Destination.VoteListFormModelScreen())
    }

    fun onOfflineButtonClicked():Unit {
        // Функционал для кнопки "Offline"
    }
}