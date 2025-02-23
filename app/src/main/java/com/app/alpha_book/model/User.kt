package com.app.alpha_book.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val phone: String?,
    val avatar: String?,
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
