package com.flexberry.androidodataofflinesample.ui.votelistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.VoteRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoteListFormViewModel @Inject constructor(
    private val repository: VoteRepository,
    @AppState private val applicationState: ApplicationState
) : ViewModel() {
    fun addVote():Unit {
        // добавление нового пользователя
    }

    fun editVote():Unit {
        // изменение данных пользователя
    }

    fun deleteVote():Unit {
        // удаление пользователя
    }
}