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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme


@Composable
fun MainScreen( modifier: Modifier = Modifier, viewModel: MainViewModel = MainViewModel() ) {
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
        val btnModifier = modifier.size(200.dp)
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            mainButton(
                modifier = btnModifier,
                fontSize = 126.sp,
                text = "+",
                onClick = viewModel::addButton
            )
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            mainButton(
                modifier = btnModifier,
                fontSize = 36.sp,
                text = "Список",
                onClick = viewModel::listButton
            )
        }
        Row(
            Modifier.weight(1f).fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.End)
        )
        {
            mainButton(
                modifier = modifier.size(100.dp),
                fontSize = 18.sp,
                text = "Offline",
                onClick = viewModel::onlineButton,
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