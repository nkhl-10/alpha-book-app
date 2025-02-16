package com.app.jetpackcomposedemo.model

data class ApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?
)
