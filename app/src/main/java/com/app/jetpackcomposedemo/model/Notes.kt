package com.app.jetpackcomposedemo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notes(
    val createdAt: String,
    val content: String,
    val media: String,
    @SerialName("userId") val userId: User, // Assuming userId references the User data class
    val location: String,
    val hashtag: List<String>,  // Assuming hashtag is a list of strings
    val id: String
)