package com.flexberry.androidodataofflinesample.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
     appNavigator: AppNavigator
) : ViewModel() {

     val navigationChannel = appNavigator.navigationChannel

}