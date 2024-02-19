package com.example.githubusers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubusers.ui.screens.GithubUserDetail
import com.example.githubusers.ui.screens.GithubUsersList
import com.example.githubusers.viewmodels.GithubUserViewModel
import kotlinx.coroutines.launch

/**
 * A navigation function create routes tree between screens.
 *
 * @param githubUserViewModel
 */
@Composable
fun Navigation(
    githubUserViewModel: GithubUserViewModel,
) {
    val nav = rememberNavController()
    val userDetailsScope = rememberCoroutineScope()

    NavHost(navController = nav, startDestination = "userList") {
        composable(route = "userList") {
            GithubUsersList(
                viewModel = githubUserViewModel,
                onUserClick = { item ->
                    userDetailsScope.launch {
                        githubUserViewModel.getUserById(item.id)
                        nav.navigate("userDetails")
                    }
                }
            )
        }
        composable(
            route = "userDetails"
        ) {
            GithubUserDetail(
                viewModel = githubUserViewModel,
                nav = nav
            )
        }
    }
}