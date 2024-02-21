package com.example.githubusers.di

import android.app.Application
import androidx.room.Room
import com.example.githubusers.data.db.Database
import com.example.githubusers.util.AppConstants
import com.example.githubusers.util.AppConstants.API_GITHUB
import com.example.githubusers.util.AppConstants.API_PUNK
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt AppModule module providing Retrofit and Room database instances and also dao interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(API_GITHUB)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        ).build()

    @Singleton
    @Provides
    fun provideDb(app: Application) = Room.databaseBuilder(app, Database::class.java, "githubusers")
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    fun provideGithubUserDao(db: Database) = db.userDao()

}