package com.app.jetpackcomposedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?
)
