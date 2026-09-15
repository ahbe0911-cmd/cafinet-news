package com.cafinet.news.core.common

/**
 * Generic wrapper used by the domain/data layers to propagate success, error
 * and loading states up to the presentation layer without leaking exceptions.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable, val message: String? = throwable.message) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (Throwable, String?) -> Unit): Result<T> {
    if (this is Result.Error) action(throwable, message)
    return this
}
