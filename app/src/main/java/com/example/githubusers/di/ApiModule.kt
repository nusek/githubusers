package com.example.githubusers.di

import com.example.githubusers.data.network.api.GithubUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * Hilt Api module provides api interfaces
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): GithubUserApi = retrofit.create(GithubUserApi::class.java)

}