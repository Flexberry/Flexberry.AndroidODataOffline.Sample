package com.flexberry.androidodataofflinesample.ui.votelistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.VoteRepository
import com.flexberry.androidodataofflinesample.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.navigation.Destination
import com.flexberry.androidodataofflinesample.data.di.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoteListFormViewModel @Inject constructor(
    private val repository: VoteRepository,
    private val appNavigator: AppNavigator
    private val repository: VoteRepository,
    @AppState private val applicationState: ApplicationState
) : ViewModel() {

    fun onAddVoteButtonClicked():Unit {
        // добавление нового пользователя
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