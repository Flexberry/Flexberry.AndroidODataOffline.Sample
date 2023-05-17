package com.flexberry.androidodataofflinesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
import com.flexberry.androidodataofflinesample.ui.votelistformmodel.VoteListFormModelScreen
import com.flexberry.androidodataofflinesample.ui.votelistformmodel.VoteListFormViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteListFormActivity : ComponentActivity() {

    // Внедрение viewModel через hilt.
    private val voteListFormViewModel: VoteListFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidODataOfflineSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VoteListFormModelScreen(votes = emptyList())
                }
            }
        }
    }
}