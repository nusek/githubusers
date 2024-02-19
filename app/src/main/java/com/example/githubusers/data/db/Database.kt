package com.example.githubusers.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubusers.data.db.dao.GithubUserDao
import com.example.githubusers.data.db.entity.GithubUser

@Database(entities = [GithubUser::class], version = 5)
abstract class Database: RoomDatabase() {
    abstract fun userDao(): GithubUserDao
}