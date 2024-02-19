package com.example.githubusers.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusers.data.db.entity.GithubUser

/**
 * Github users DAO interface to manage the local database data
 *
 */
@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: GithubUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(list: List<GithubUser>)

    @Query("SELECT * from user")
    suspend fun getUsers(): List<GithubUser>

    @Query("SELECT * FROM user WHERE id=:id")
    suspend fun getUserById(id: Int): GithubUser

    @Query("SELECT * FROM user")
    fun pagingSource(): PagingSource<Int, GithubUser>

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}