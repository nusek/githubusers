package com.example.githubusers.data.network.api

import com.example.githubusers.data.network.dto.GithubUserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Github api interface with suspend function getting data from API
 *
 */
interface GithubUserApi {

    @GET("users")
    suspend fun getGithubUsers(): List<GithubUserDto>

    @GET("users")
    suspend fun getGithubUsersPaging(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int
    ): List<GithubUserDto>

    @GET("users/{id}")
    suspend fun getGithubUser(
        @Path("id") id: Int
    ): GithubUserDto
}