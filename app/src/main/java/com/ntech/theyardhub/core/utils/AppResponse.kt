package com.ntech.theyardhub.core.utils

sealed interface AppResponse<out T> {
    object Loading : AppResponse<Nothing>

    open class Error(
        open val message: String,
        val meta: Map<String, Any?> = mapOf(),
    ) : AppResponse<Nothing>

    object Empty : AppResponse<Nothing>

    class Success<T>(
        val data: T,
        val meta: Map<String, Any?> = mapOf(),
    ) : AppResponse<T>
}
