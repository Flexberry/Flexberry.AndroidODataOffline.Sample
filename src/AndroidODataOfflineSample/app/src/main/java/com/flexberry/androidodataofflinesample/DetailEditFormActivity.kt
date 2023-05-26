package com.flexberry.androidodataofflinesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flexberry.androidodataofflinesample.ui.detaileditform.DetailEditFormScreen
import com.flexberry.androidodataofflinesample.ui.detaileditform.DetailEditFormViewModel
import com.flexberry.androidodataofflinesample.ui.mastereditformmodel.MasterEditFormScreen
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailEditFormActivity : ComponentActivity() {

    // Внедрение viewModel через hilt.
    private val detailEditFormViewModel: DetailEditFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidODataOfflineSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailEditFormScreen()
                }
            }
        }
    }
}