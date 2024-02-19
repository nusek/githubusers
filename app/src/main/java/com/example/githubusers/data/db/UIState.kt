package com.example.githubusers.data.db

import com.example.githubusers.data.db.entity.GithubUser

sealed class UIState {
    data class Home(val users: List<GithubUser>)
    data class UserDetails(val user: GithubUser)
}
