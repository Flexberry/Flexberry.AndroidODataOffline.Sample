package com.flexberry.androidodataofflinesample.ui.mainmodel

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun MainScreen( modifier: Modifier = Modifier ) {
    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        mainButton(modifier = modifier, fontSize = 126.sp, text = "+")
        mainButton(modifier = modifier, fontSize = 36.sp, text = "Список")

        Column(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.End
        )
        {
            Button(
                modifier = modifier
                    .size(100.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Offline",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
fun mainButton(modifier: Modifier, fontSize: TextUnit, text: String) {
    Button(
        modifier = modifier
            .size(200.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
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