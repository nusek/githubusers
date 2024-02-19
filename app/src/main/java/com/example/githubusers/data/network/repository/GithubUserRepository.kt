package com.example.githubusers.data.network.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubusers.data.db.dao.GithubUserDao
import com.example.githubusers.data.db.entity.GithubUser
import com.example.githubusers.data.network.api.GithubUserApi
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A Github user repository class with suspend function getting data from api, and save into local database
 *
 * @property api
 * @property dao
 */
@Singleton
class GithubUserRepository @Inject constructor(
    val api: GithubUserApi,
    private val dao: GithubUserDao
) :Repository() {

    suspend fun getGithubUsers() = safeApiCall {
        val result = api.getGithubUsers()
        val users = result.map { GithubUser(it) }
        if (users.isNotEmpty()) dao.saveUsers(users)
        dao.getUsers()
    }


    suspend fun getGithubUser(id: Int) = safeApiCall {
        val result = api.getGithubUser(id)
        val user = GithubUser(result)
        dao.saveUser(user)
        dao.getUserById(id)
    }

    suspend fun getGithubUserFromDb(id: Int) = dao.getUserById(id)

    suspend fun getGithubUsersFromDb() = dao.getUsers()

}