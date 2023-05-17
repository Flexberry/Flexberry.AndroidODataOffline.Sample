package com.flexberry.androidodataofflinesample.ui.votelistformmodel

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.data.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoteListFormViewModel @Inject constructor(
    private val repository: VoteRepository
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