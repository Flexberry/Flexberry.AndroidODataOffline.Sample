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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiStateOnline = remember {
        viewModel.applicationState.isOnline
    }

    Column(
        modifier = modifier
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
                text = "Master",
                rounded = 10,
                onClick = viewModel::onMasterButtonClicked
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
                text = "Detail",
                rounded = 10,
                onClick = viewModel::onDetailButtonClicked
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
                text = "Test data",
                textColor = MaterialTheme.colorScheme.onPrimary,
                rounded = 50,
                onClick = viewModel::onInitTestData
            )

            var buttonText = "Offline"
            var buttonTextColor = MaterialTheme.colorScheme.onSecondary
            if (uiStateOnline.value) {
                buttonText = "Online"
                buttonTextColor = MaterialTheme.colorScheme.onPrimary
            }
            mainButton(
                modifier = modifier.size(100.dp),
                fontSize = 18.sp,
                text = buttonText,
                textColor = buttonTextColor,
                rounded = 50,
                onClick = viewModel::onOfflineButtonClicked
            )
        }
    }
}

@Composable
fun mainButton(
    modifier: Modifier,
    fontSize: TextUnit,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    rounded: Int = 0,
    onClick: () -> Unit,
    ) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(rounded),
        onClick = { onClick() },
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontSize = fontSize,
            color = textColor
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