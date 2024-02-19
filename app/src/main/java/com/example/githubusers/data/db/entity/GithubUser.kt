package com.example.githubusers.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubusers.data.network.dto.GithubUserDto
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class GithubUser(
    @PrimaryKey
    val id: Int,
    val login: String,
    val pictureUrl: String?,
    val imageUrl: String?,
    val gravatarId: String?,
    val url: String? = null,
    val htmlUrl: String? = null,
    val followersUrl: String? = null,
    val followingUrl: String? = null,
    val gistsUrl: String? = null,
    val starredUrl: String? = null,
    val subscriptionsUrl: String? = null,
    val organizationsUrl: String? = null,
    val reposUrl: String? = null,
    val eventsUrl: String? = null,
    val receivedEventsUrl: String? = null,
    val type: String? = null,
    val siteAdmin: Boolean? = null,
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val hireable: Boolean? = null,
    val bio: String? = null,
    val twitterUsername: String? = null,
    val publicRepos: Int? = null,
    val publicGists: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {

    constructor(dto: GithubUserDto) : this(
        dto.id,
        dto.login ?: "",
        dto.pictureUrl,
        dto.imageUrl,
        dto.gravatarId,
        dto.url,
        dto.htmlUrl,
        dto.followersUrl,
        dto.followingUrl,
        dto.gistsUrl,
        dto.starredUrl,
        dto.subscriptionsUrl,
        dto.organizationsUrl,
        dto.reposUrl,
        dto.eventsUrl,
        dto.receivedEventsUrl,
        dto.type,
        dto.siteAdmin,
        dto.name,
        dto.company,
        dto.blog,
        dto.location,
        dto.email,
        dto.hireable,
        dto.bio,
        dto.twitterUsername,
        dto.publicRepos,
        dto.publicGists,
        dto.followers,
        dto.following,
        dto.createdAt,
        dto.updatedAt
    )
}
