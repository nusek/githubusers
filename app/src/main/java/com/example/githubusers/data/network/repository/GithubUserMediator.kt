package com.example.githubusers.data.network.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubusers.data.db.dao.GithubUserDao
import com.example.githubusers.data.db.entity.GithubUser
import com.example.githubusers.data.network.api.GithubUserApi
import java.io.IOException

/**
 * A paging mediator class extending RemoteMediator from androidx paging library
 *
 * @property api
 * @property dao
 */
@OptIn(ExperimentalPagingApi::class)
class GithubUserMediator(
    private val api: GithubUserApi,
    private val dao: GithubUserDao
) : RemoteMediator<Int, GithubUser>() {


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubUser>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val users = api.getGithubUsersPaging(
                page = loadKey,
                pageCount = state.config.pageSize
            )

            if (loadType == LoadType.REFRESH) {
                dao.deleteAll()
            }
            val githubUsers = users.map { GithubUser(it) }
            dao.saveUsers(githubUsers)

            MediatorResult.Success(
                endOfPaginationReached = users.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}