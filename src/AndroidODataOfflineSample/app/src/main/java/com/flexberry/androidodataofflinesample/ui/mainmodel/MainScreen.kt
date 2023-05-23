package com.flexberry.androidodataofflinesample.ui.mainmodel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    val uiStateOnline = remember {
        viewModel.applicationState.isOnline
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(
                vertical = 40.dp,
                horizontal = 20.dp
            ),

    ) {
        val rowModifier = modifier
            .padding(bottom = 18.dp)
            .weight(2f)
            .fillMaxSize()
        val btnModifier = modifier.size(250.dp)
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            mainButton(
                modifier = btnModifier,
                fontSize = 24.sp,
                text = "ApplicationUser",
                rounded = 10,
                onClick = viewModel::appUserButton
            )
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            mainButton(
                modifier = btnModifier,
                fontSize = 24.sp,
                text = "Vote",
                rounded = 10,
                onClick = viewModel::voteButton
            )
        }
        Row(
            Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.End)
        )
        {
            mainButton(
                modifier = modifier.size(100.dp),
                fontSize = 18.sp,
                text = if (uiStateOnline.value) { "Online" } else { "Offline" },
                onClick = viewModel::offlineButton,
                rounded = 50
            )
        }
    }
}

@Composable
fun mainButton(
    modifier: Modifier,
    fontSize: TextUnit,
    text: String,
    onClick: () -> Unit,
    rounded: Int = 0
    ) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(rounded),
        onClick = { onClick() },
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontSize = fontSize
        )
    }
}

// Для предпросмотра в Android Studio
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode",
    showBackground = true,
)
@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Composable
fun MainScreenPreview() {
    AndroidODataOfflineSampleTheme {
        MainScreen()
    }
}