package com.example.githubusers.data.network.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class Repository : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    /**
     * a method that takes care of performing an API request on the background thread.
     *
     * @param dispatcher - a coroutine dispatcher on which we want to perfrom the task.
     * @param apiCall - a suspended API call that we want to perform.
     *
     * @return Result - a class that contains information on API response, errors if they occured, and response data if it is present.
     */
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.Default, apiCall: suspend () -> T): Result<T> {
        return withContext(dispatcher) {
            try {
                Result.Success(apiCall.invoke())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}