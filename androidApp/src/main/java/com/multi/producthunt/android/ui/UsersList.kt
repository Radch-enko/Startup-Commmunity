package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.multi.producthunt.android.R
import com.multi.producthunt.android.ui.theme.Shapes
import com.multi.producthunt.ui.models.SearchUserUI

@Composable
fun UsersList(
    pagingList: LazyPagingItems<SearchUserUI>,
    scrollState: LazyListState = rememberLazyListState(),
    firstItemPaddingTop: Dp = 0.dp,
    onUserClick: (id: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        state = scrollState
    ) {
        item {
            Spacer(modifier = Modifier.height(firstItemPaddingTop))
        }
        this.applyStates(
            pagingList,
            onUserClick = onUserClick
        )
    }
}

private val usersRowModifier =
    Modifier.padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

private fun LazyListScope.applyStates(
    list: LazyPagingItems<SearchUserUI>,
    onUserClick: (id: Int) -> Unit,
) {
    this.apply {
        userRow(list, onUserClick = onUserClick)
        when {
            list.loadState.refresh is LoadState.Error -> {
                val e = list.loadState.refresh as LoadState.Error
                item {
                    LoadStateError(e.error.localizedMessage)
                }
            }
            list.loadState.refresh is LoadState.Loading -> {
                val placeHolderList = (1..5).map { SearchUserUI.Placeholder }
                placeholderUsersList(placeHolderList)
            }
            list.loadState.append is LoadState.Loading -> {
                val placeHolderList = (1..2).map { SearchUserUI.Placeholder }
                placeholderUsersList(placeHolderList)
            }
            list.loadState.append is LoadState.Error -> {
                val e = list.loadState.append as LoadState.Error
                item {
                    LoadStateAppendError(e.error.message)
                }
            }
            list.loadState.source.refresh is LoadState.NotLoading && list.loadState.append.endOfPaginationReached && list.itemCount < 1 -> {
                item {
                    NoItemsMessage()
                }
            }
        }
    }
}

private fun LazyListScope.placeholderUsersList(
    list: List<SearchUserUI>
) {
    this.items(list) { searchUser ->
        Box(modifier = usersRowModifier) {
            UserRow(searchUser, placeHolderVisible = true, onUserClick = {})
        }
    }
}

private fun LazyListScope.userRow(
    list: LazyPagingItems<SearchUserUI>,
    onUserClick: (id: Int) -> Unit
) {
    this.items(list) { searchUser ->
        Box(modifier = usersRowModifier) {
            if (searchUser != null) {
                UserRow(
                    searchUser,
                    onUserClick = onUserClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRow(
    searchUser: SearchUserUI,
    placeHolderVisible: Boolean = false,
    onUserClick: (id: Int) -> Unit
) {
    androidx.compose.material3.Surface(
        shape = Shapes.medium,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        onClick = {
            onUserClick(searchUser.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoadableImage(
                link = searchUser.profileImage, modifier = Modifier
                    .size(80.dp),
                errorDrawable = R.drawable.no_profile_image
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = searchUser.username,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
                Spacer(modifier = Modifier.height(16.dp))
                MediumText(
                    text = searchUser.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
            }
        }
    }
}
