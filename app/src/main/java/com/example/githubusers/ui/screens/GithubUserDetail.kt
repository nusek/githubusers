package com.example.githubusers.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.githubusers.R
import com.example.githubusers.data.db.UIState
import com.example.githubusers.data.db.entity.GithubUser
import com.example.githubusers.data.network.dto.GithubUserDto
import com.example.githubusers.viewmodels.GithubUserViewModel

/**
 * Single user details
 *
 * @param viewModel
 * @param nav
 */
@Composable
fun GithubUserDetail(
    viewModel: GithubUserViewModel,
    nav: NavController?
) {

    val user by viewModel.userFLow.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        AppBar(
            title = "User Details",
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        ) {
            nav?.navigateUp()
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            UserImage(user, 300.dp)
            UserContent(user, Alignment.CenterHorizontally)

        }
    }

}

/**
 * AppBar composable function with TopAppBar title argument and back arrow icon action
 *
 * @param title
 * @param imageVector
 * @param iconClickAction
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, imageVector: ImageVector, iconClickAction: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { iconClickAction.invoke() }) {
                Icon(imageVector = imageVector, contentDescription = "")
            }
        }
    )
}

/**
 * User image composable function with picSize in argument of the function
 *
 * @param user
 * @param picSize
 */
@Composable
fun UserImage(user: GithubUser?, picSize: Dp) {

    Card(
        modifier = Modifier
            .padding(16.dp),
        shape = RectangleShape
    ) {
        if (user != null) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.pictureUrl ?: user.imageUrl)
                    .crossfade(true)
                    .build(),
                loading = { LinearProgressIndicator() },
                contentDescription = user.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(picSize),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "No image", textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp, color = Color.Red
            )
        }
    }
}

/**
 * User content composable function with alignment in argument.
 * The function building a user content by the data from GithubUser model.
 *
 * @param user
 * @param alignment
 */
@Composable
fun UserContent(user: GithubUser?, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = alignment,
    ) {
        if (user != null) {
            UserName(user)
            Spacer(modifier = Modifier.padding(16.dp))
            UserEmail(user.email)
            Spacer(modifier = Modifier.padding(16.dp))
            UserLocation(location = user.location)
        } else {
            Text(
                text = "No user data", textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp, color = Color.Red
            )
        }
    }
}

/**
 * User name composable function displaying user name else user login which is always provides( getting when the user details click )
 *
 * @param user
 */
@Composable
fun UserName(user: GithubUser) {
    Text(
        text = user.name ?: user.login,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp, color = Color.Black
    )
}

/**
 * User email composable function displaying user email
 *
 * @param email
 */
@Composable
fun UserEmail(email: String?) {
    Text(
        text = email ?: "No data",
        textAlign = TextAlign.Center,
        fontSize = 18.sp, color = Color.Gray
    )
}

/**
 * User location composable function displaying
 *
 * @param location
 */
@Composable
fun UserLocation(location: String?) {
    Text(
        text = location ?: "No data",
        textAlign = TextAlign.End,
        fontSize = 16.sp, color = Color.Gray
    )
}

/**
 * A preview composable function with example GithubUser data
 *
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserDetailsPreview() {

    val user = GithubUser(
        GithubUserDto(
            login = "Jason",
            id = 1,
            nodeId = null,
            pictureUrl = "https://images.punkapi.com/v2/keg.png",
            email = "jason@emial.com",
            location = "Boston"
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        UserImage(user, 300.dp)
        UserContent(user, Alignment.CenterHorizontally)
    }
}