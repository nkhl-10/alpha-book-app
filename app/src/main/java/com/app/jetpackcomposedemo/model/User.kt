package com.app.jetpackcomposedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val createdAt: String,
    val username: String,
    val avatar: String,
    val email: String,
    val password: String,
    val bio: String,
    val id: String
)