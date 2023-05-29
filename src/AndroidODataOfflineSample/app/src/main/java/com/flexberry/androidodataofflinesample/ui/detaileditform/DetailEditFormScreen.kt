package com.flexberry.androidodataofflinesample.ui.detaileditform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexberry.androidodataofflinesample.ui.theme.editFormTopMenu

@Composable
fun DetailEditFormScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailEditFormViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            editFormTopMenu(
                modifier = modifier,
                onCloseButtonClicked = viewModel::onCloseDetailClicked,
                onSaveCloseButtonClicked = viewModel::onSaveCloseDetailClicked,
                onSaveButtonClicked = viewModel::onSaveDetailClicked
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
                        text = "Detail: ",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                    Text(
                        modifier = modifier,
                        text = viewModel.detail.primarykey.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = modifier.size(16.dp))
                EditItem(fieldName = "Name", value = viewModel.detailName)

                Spacer(modifier = modifier.size(16.dp))
                Item(fieldName = "Master name", value = viewModel.detail.master.name.toString())
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItem(
    modifier: Modifier = Modifier,
    fieldName: String,
    value: MutableState<String>,
) {
    var mutableValue  by remember { value }

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
            value = mutableValue ,
            onValueChange = { mutableValue = it },
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Item(
    modifier: Modifier = Modifier,
    fieldName: String,
    value: String,
) {
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
            value = value ,
            onValueChange = { },
            maxLines = 1,
            enabled = false
        )
    }
}