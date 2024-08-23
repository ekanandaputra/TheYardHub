package com.ntech.theyardhub.core.utils

fun convertToInt(anyValue: Any): Int? {
    return when (anyValue) {
        is Int -> anyValue
        is String -> anyValue.toIntOrNull()
        else -> null
    }
}
