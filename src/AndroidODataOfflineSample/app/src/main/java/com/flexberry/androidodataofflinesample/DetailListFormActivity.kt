package com.flexberry.androidodataofflinesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flexberry.androidodataofflinesample.ui.detaillistform.DetailListFormScreen
import com.flexberry.androidodataofflinesample.ui.detaillistform.DetailListFormViewModel
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailListFormActivity : ComponentActivity() {

    // Внедрение viewModel через hilt.
    private val detailListFormViewModel: DetailListFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidODataOfflineSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailListFormScreen(details = emptyList())
                }
            }
        }
    }
}