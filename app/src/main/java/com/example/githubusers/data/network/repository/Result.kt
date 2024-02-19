package com.example.githubusers.data.network.repository

sealed class Result<out T> {
    /**
     * There were no exceptions during fetching the data from API.
     * @param data - data responded from API
     */
    class Success<out T>(val data: T): Result<T>()

    /**
     * There was a caught exception during the query or API response
     * @param exception - caught exception
     * @param code - code of API response if there was a HttpException
     * @param message - exception message
     */
    class Error(val exception: Throwable, val code: Int? = null, val message: String? = exception.localizedMessage): Result<Nothing>()
}
