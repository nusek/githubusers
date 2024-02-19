package com.example.githubusers.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.githubusers.data.db.dao.GithubUserDao
import com.example.githubusers.data.network.api.GithubUserApi
import com.example.githubusers.data.network.repository.GithubUserMediator
import com.example.githubusers.data.network.repository.GithubUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository classes and also PagingMediator
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGithubUserRepository(dao: GithubUserDao, api: GithubUserApi) =
        GithubUserRepository(api, dao)

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun provideUsersPager(api: GithubUserApi, dao: GithubUserDao) = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = GithubUserMediator(
            api = api,
            dao = dao
        ),
        pagingSourceFactory = {
            dao.pagingSource()
        }
    )
}