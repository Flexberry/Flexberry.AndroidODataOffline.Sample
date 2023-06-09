package com.flexberry.androidodataofflinesample.ui.mastereditformmodel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexberry.androidodataofflinesample.ui.detaillistform.ListItem
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
import com.flexberry.androidodataofflinesample.ui.theme.editFormTopMenu

@Composable
fun MasterEditFormScreen(
    modifier: Modifier = Modifier,
    viewModel: MasterEditFormViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            editFormTopMenu(
                modifier = modifier,
                onCloseButtonClicked = viewModel::onCloseMasterClicked,
                onSaveCloseButtonClicked = viewModel::onSaveCloseMasterClicked,
                onSaveButtonClicked = viewModel::onSaveMasterClicked
            )

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
                        text = viewModel.dataObject.primarykey.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = modifier.size(16.dp))
                EditItem(viewModel = viewModel, fieldName = "Name")

                Spacer(modifier = modifier.size(16.dp))
                Text(modifier = modifier, text = "Details")

                Spacer(modifier = modifier.size(8.dp))
                val details = viewModel.dataObject.details?.toList()
                if (details != null) {
                    LazyColumn(modifier = modifier) {
                        items(details) { detail ->
                            ListItem(detail)
                        }
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItem(
    modifier: Modifier = Modifier,
    fieldName: String,
    viewModel: MasterEditFormViewModel
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = modifier.weight(1f),
            text = fieldName
        )
        OutlinedTextField(
            modifier = modifier.weight(2f),
            value = viewModel.mutableName,
            onValueChange = { viewModel.mutableName = it },
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