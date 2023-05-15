package com.flexberry.androidodataofflinesample.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel.ListItems
import com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel.SampleData
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme

class ApplicationUserListFormActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidODataOfflineSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListItems(SampleData.usersSample)
                }
            }
        }
    }
}