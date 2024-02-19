package com.example.githubusers.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.githubusers.data.db.UIState
import com.example.githubusers.data.db.entity.GithubUser
import com.example.githubusers.data.network.dto.GithubUserDto
import com.example.githubusers.ui.theme.Purple40
import com.example.githubusers.ui.theme.Purple80
import com.example.githubusers.viewmodels.GithubUserViewModel

/**
 * List of github users
 *
 * @param viewModel
 * @param onUserClick
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUsersList(
    viewModel: GithubUserViewModel,
    onUserClick: (GithubUser) -> Unit
) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Users List", color = Color.Black) },
            )
        }
    ) { paddings ->
        Column(
            Modifier
                .fillMaxSize()
//                .verticalScroll(scrollState)
                .padding(paddings)
        ) {
            PaginationButton(viewModel)
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = searchText,
                onQueryChange = viewModel::onSearchTextChange,
                onSearch = viewModel::onSearchTextChange,
                active = isSearching,
                onActiveChange = { viewModel.onToggleSearch() },
                placeholder = { Text(text = "Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty())
                        Icon(
                            modifier = Modifier.clickable { viewModel.clearSearchBar() },
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                },
            ) {
                UsersList(
                    viewModel = viewModel,
                    onUserClick = onUserClick
                )
            }
            if (!isSearching)
                UsersList(
                    viewModel = viewModel,
                    onUserClick = onUserClick
                )
        }
    }
}

/**
 * A single user card composable function displaying all user data from GithubUser model on the users list
 *
 * @param user
 * @param clickAction
 */
@Composable
fun UserCard(user: GithubUser, clickAction: (GithubUser) -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable { clickAction(user) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            UserPicture(user = user, picSize = 70.dp)
            UserListContent(user = user, alignment = Alignment.Start)
        }
    }
}

/**
 * a user picture composable function displaying user photo on the single list item by picSize provides in function argument.
 *
 * @param user
 * @param picSize
 */
@Composable
fun UserPicture(user: GithubUser, picSize: Dp) {
    Card(
        shape = CircleShape,
        modifier = Modifier.padding(16.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    user.pictureUrl ?: user.imageUrl
                ) // this is because of problems with github rate limits and i try to download data from AppConstants API_PUNK
                .crossfade(true)
                .build(),
            loading = { CircularProgressIndicator() },
            contentDescription = user.name,
            modifier = Modifier.size(picSize),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * A user list content composable function displaying just the user name (or login when name isn't provides) near photo
 *
 * @param user
 * @param alignment
 */
@Composable
fun UserListContent(user: GithubUser, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Text(text = user.name ?: user.login, style = MaterialTheme.typography.bodyMedium)
    }

}

/**
 * A composable user list function get from viewModel github users when the pagination is disabled
 *
 * @param viewModel
 * @param onUserClick
 */
@Composable
fun UsersList(
    viewModel: GithubUserViewModel,
    onUserClick: (GithubUser) -> Unit
) {
    val showProgress by viewModel.showProgress.collectAsStateWithLifecycle()
    val searchFlow by viewModel.searchFlow.collectAsStateWithLifecycle()
    val usersFlow by viewModel.usersFlow.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val isPagination by viewModel.isPagination.collectAsStateWithLifecycle()

    if (isPagination) {
        PagingUsersList(viewModel, onUserClick)
        return
    }

    LaunchedEffect(key1 = viewModel.usersFlow) {
        viewModel.getUsers()
    }

    val users = if (isSearching) searchFlow else usersFlow

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(users.size) { index ->
            val user = users[index]
            UserCard(
                user = user,
                clickAction = { onUserClick(user) }
            )
        }
        item {
            if (showProgress) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

}

/**
 * A pagination composable function displaying github user list.
 *
 * @param viewModel
 * @param onUserClick
 */
@Composable
fun PagingUsersList(
    viewModel: GithubUserViewModel,
    onUserClick: (GithubUser) -> Unit
) {

    val users = viewModel.userPagingFlow.collectAsLazyPagingItems()
    val showError by viewModel.showError.collectAsStateWithLifecycle()
    val ctx = LocalContext.current
    LaunchedEffect(key1 = users.loadState) {
        if (users.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                ctx,
                "Error: " + (users.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    if (showError) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Problem of getting data",
                modifier = Modifier.fillMaxSize(),
                color = Color.Red
            )
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (users.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(count = users.itemCount) { index ->
                    val user = users[index]
                    user?.let {
                        UserCard(
                            user = user,
                            clickAction = { onUserClick(user) }
                        )
                    }

                }
                item {
                    if (users.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

/**
 * A pagination button composable function with a possibility to turn off/on pagination mechanism
 * (this button is because Github API return API rate limit and blocks users list downloading on the some time)
 *
 * @param viewModel
 */
@Composable
fun PaginationButton(viewModel: GithubUserViewModel) {
    val isPaginationEnabled by viewModel.isPagination.collectAsStateWithLifecycle()
    IconButton(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
        .background(Purple80)
        .border(
            width = 1.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(10)
        ),
        onClick = { viewModel.onPaginationChange() }) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            text = if (isPaginationEnabled) "Disable pagination" else "Enable pagination",
            color = Color.White
        )
    }
}

/**
 * A github users list preview composable function
 *
 */
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GithubUserListPreview() {
    val users = listOf(
        GithubUser(GithubUserDto("John", 1)),
        GithubUser(GithubUserDto("Mark", 2))
    )
}

