package com.flexberry.androidodataofflinesample.ui.votelistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.data.VoteRepository
import com.flexberry.androidodataofflinesample.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoteListFormViewModel @Inject constructor(
    private val repository: VoteRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun onAddVoteButtonClicked():Unit {
        // добавление нового пользователя
       // appNavigator.tryNavigateBack()
    }

    fun onEditVoteClicked():Unit {
        // изменение данных пользователя
    }

    fun onDeleteVoteClicked():Unit {
        // удаление пользователя
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }

}