package com.flexberry.androidodataofflinesample.ui.mastereditformmodel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexberry.androidodataofflinesample.ui.detaillistform.ListItem
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme

@Composable
fun MasterEditFormScreen(
    modifier: Modifier = Modifier,
    viewModel: MasterEditFormViewModel = hiltViewModel(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            editFormTopMenu(viewModel = viewModel)

            Column(
                modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {

                Spacer(modifier = modifier.size(16.dp))
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier,
                        text = "Master: ",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                    Text(
                        modifier = modifier,
                        text = viewModel.master.primarykey.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = modifier.size(16.dp))
                EditItem(viewModel = viewModel, fieldName = "Name")

                Spacer(modifier = modifier.size(16.dp))
                Text(modifier = modifier, text = "Details")

                Spacer(modifier = modifier.size(8.dp))
                val details = viewModel.master.details!!.toList()
                LazyColumn(modifier = modifier) {
                    items(details) { detail ->
                        ListItem(detail)
                    }
                }
            }
        }
    }
}

@Composable
fun editFormTopMenu(
    modifier: Modifier = Modifier,
    viewModel: MasterEditFormViewModel = hiltViewModel()
) {
    Row(modifier = modifier) {
        Row(modifier = modifier.weight(1f)) {
            editFormTopMenuButton(text = "Close", onButtonClicked = viewModel::onCloseButtonClicked)
        }

        Spacer(modifier = modifier.size(16.dp))

        Row(
            modifier = modifier.weight(2f),
            horizontalArrangement = Arrangement.End
        ) {
            editFormTopMenuButton(text = "Save & Close", onButtonClicked = viewModel::onSaveButtonClicked)

            Spacer(modifier = modifier.size(8.dp))

            editFormTopMenuButton(text = "Save", onButtonClicked = viewModel::onSaveButtonClicked)
        }
    }
}

@Composable
fun editFormTopMenuButton(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClicked: () -> Unit
) {
    val buttonSize = 90.dp
    Button(
        modifier = modifier
            .size(buttonSize),
        onClick = { onButtonClicked() }
    ) {
        Text(
            text = text,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItem(
    modifier: Modifier = Modifier,
    fieldName: String,
    viewModel: MasterEditFormViewModel
) {
    var name by remember { mutableStateOf(viewModel.master.name) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = modifier.weight(1f),
            text = fieldName
        )
        TextField(
            modifier = modifier.weight(2f),
            value = name.toString(),
            onValueChange = {
                name = it
                viewModel.master.name = it
                            },
            maxLines = 1
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
fun ListItemsPreview() {
    AndroidODataOfflineSampleTheme {
        MasterEditFormScreen()
    }
}