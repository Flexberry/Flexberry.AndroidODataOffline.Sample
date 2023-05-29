package com.flexberry.androidodataofflinesample.ui.detaillistform

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexberry.androidodataofflinesample.data.model.Detail
import com.flexberry.androidodataofflinesample.ui.theme.listFormBottomMenu

@Composable
fun DetailListFormScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailListFormViewModel = hiltViewModel()
) {
    val detailsList = remember { viewModel.details }
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column() {
            Text(
                modifier = modifier.padding(start = 16.dp),
                text = "Detail List",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 48.sp
            )
            LazyColumn(
                modifier = modifier
                    .padding(start = 32.dp, top = 16.dp, end = 32.dp, bottom = 16.dp)
            ) {
                items(detailsList.value) { detail ->
                    ListItem(detail, viewModel)
                }
            }
        }

        listFormBottomMenu(
            onAddItemButtonClicked = { viewModel.onAddDetailButtonClicked() },
            onBackButtonClicked = { viewModel.onBackButtonClicked() }
        )
    }

}

@Composable
fun ListItem(
    detail: Detail,
    viewModel: DetailListFormViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var isExpandedItem by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .padding(bottom = 8.dp)
            .border(1.5.dp, MaterialTheme.colorScheme.primary)
            .clickable { isExpandedItem = !isExpandedItem }
    ) {

        Column(
            modifier = modifier
                .padding(all = 16.dp)
        ) {
            Text(
                text = detail.name ?: "",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = modifier.height(8.dp))
        }

        var isExpandedMenu by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = { isExpandedMenu = !isExpandedMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Подробнее",
                    Modifier.rotate(90f)
                )
            }

            DropdownMenu(
                expanded = isExpandedMenu,
                onDismissRequest = { isExpandedMenu = false },

                ) {
                DropdownMenuItem(
                    text = { Text("Редактировать") },
                    onClick = { viewModel.onEditDetailClicked(detail) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Удалить") },
                    onClick = { viewModel.onDeleteDetailClicked(detail) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    },
                )
            }
        }
    }
}