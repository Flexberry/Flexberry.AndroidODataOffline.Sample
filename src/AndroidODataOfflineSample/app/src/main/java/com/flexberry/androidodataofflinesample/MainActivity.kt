package com.flexberry.androidodataofflinesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.flexberry.androidodataofflinesample.navigation.BaseNavigation
import com.flexberry.androidodataofflinesample.ui.mainmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Внедрение viewModel через hilt.
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseNavigation()
        }
    }
}