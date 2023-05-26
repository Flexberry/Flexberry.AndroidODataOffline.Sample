package com.flexberry.androidodataofflinesample.ui.detaillistform

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexberry.androidodataofflinesample.ApplicationState
import com.flexberry.androidodataofflinesample.data.DetailRepository
import com.flexberry.androidodataofflinesample.data.di.AppState
import com.flexberry.androidodataofflinesample.data.model.Detail
import com.flexberry.androidodataofflinesample.ui.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailListFormViewModel@Inject constructor(
    private val repository: DetailRepository,
    @AppState private val applicationState: ApplicationState,
    private val appNavigator: AppNavigator
) : ViewModel() {

    val details = mutableStateOf(getDetails(applicationState.isOnline.value))

    init {
        // Пример слежки за изменением онлайна.
        snapshotFlow { applicationState.isOnline.value }.onEach { isOnline ->
            details.value = getDetails(isOnline)
        }
        .launchIn(viewModelScope)
    }

    fun onAddDetailButtonClicked():Unit {
        // добавление нового пользователя
    }

    fun onEditDetailClicked():Unit {
        // изменение данных пользователя
    }

    fun onDeleteDetailClicked():Unit {
        // удаление пользователя
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack(Destination.MainScreen())
    }

    private fun getDetails(isOnline: Boolean): List<Detail> {
        return if (isOnline) {
            repository.getDetailsOnline().take(10)
        } else {
            repository.getDetailsOffline().take(10)
        }
    }
}