package com.flexberry.androidodataofflinesample.ui.votelistformmodel

import android.content.res.Configuration
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flexberry.androidodataofflinesample.R
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.model.Vote
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
import com.flexberry.androidodataofflinesample.ui.theme.listFormBottomMenu


@Composable
fun VoteListFormModelScreen(
    modifier: Modifier = Modifier,
    viewModel: VoteListFormViewModel = viewModel(),
    votes: List<Vote>
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column() {
            LazyColumn(
                modifier = modifier
                    .padding(start = 32.dp, top = 16.dp, end = 32.dp, bottom = 16.dp)
            ) {
                items(votes) { vote ->
                    ListItem(vote, viewModel)
                }
            }
        }

        listFormBottomMenu(addItemFun = viewModel::addVote)
    }
}

@Composable
fun ListItem(
    vote: Vote,
    viewModel: VoteListFormViewModel = viewModel(),
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
                text = vote.creator,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = modifier.height(8.dp))
            ItemData(text = vote.createTime.toString())

            if (isExpandedItem) {
                ItemData(text = vote.editor)
                ItemData(text = vote.editTime.toString())
                ItemData(
                    text = "",
                    painterResourceId = when (vote.voteType) {
                            VoteType.Like -> R.drawable.thumb_up_fill0_wght400_grad0_opsz48
                            VoteType.Dislike -> R.drawable.thumb_down_fill0_wght400_grad0_opsz48
                            else -> null
                        }
                )
            }
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
                    onClick = { viewModel::editVote },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Удалить") },
                    onClick = { viewModel::deleteVote },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    },
                )
            }
        }
    }
}

@Composable
fun ItemData(text: String, painterResourceId: Int? = null) {
    Row() {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
        if (painterResourceId != null) {
            Icon(
                painter = painterResource(painterResourceId),
                contentDescription = null,
            )
        }
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
fun ListItemsPreview() {
    AndroidODataOfflineSampleTheme {
        VoteListFormModelScreen(votes = emptyList())
    }
}