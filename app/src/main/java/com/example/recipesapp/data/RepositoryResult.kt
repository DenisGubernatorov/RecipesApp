package com.example.recipesapp.data

sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val exception: Throwable) : RepositoryResult<Nothing>()
}