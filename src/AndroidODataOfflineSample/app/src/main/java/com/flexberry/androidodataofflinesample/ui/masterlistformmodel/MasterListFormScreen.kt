package com.flexberry.androidodataofflinesample.ui.masterlistformmodel

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.ui.theme.listFormBottomMenu


@Composable
fun MasterListFormScreen(
    modifier: Modifier = Modifier,
    viewModel: MasterListFormViewModel = hiltViewModel(),
    masters: List<Master> = emptyList()
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column() {
            LazyColumn(
                modifier = modifier
                    .padding(start = 32.dp, top = 16.dp, end = 32.dp, bottom = 16.dp)
            ) {
                items(masters) { master ->
                    ListItem(master, viewModel)
                }
            }
        }

        listFormBottomMenu(
            onAddItemButtonClicked = { viewModel.onAddMasterButtonClicked() },
            onBackButtonClicked = { viewModel.onBackButtonClicked() }
        )
    }

}

@Composable
fun ListItem(
    master: Master,
    viewModel: MasterListFormViewModel = hiltViewModel(),
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
                text = master.name ?: "",
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
                    onClick = { viewModel::onEditMasterClicked },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Удалить") },
                    onClick = { viewModel::onDeleteMasterClicked },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    },
                )
            }
        }
    }
}

