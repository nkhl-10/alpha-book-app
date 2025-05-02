package com.app.alpha_book.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int = 0,
    val username: String,
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val avatar: String? = null,
)
