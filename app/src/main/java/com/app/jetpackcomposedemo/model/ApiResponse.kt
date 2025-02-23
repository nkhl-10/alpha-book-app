package com.app.jetpackcomposedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Int? = null,
    val data: T? = null,
    val message: String? = null
)
