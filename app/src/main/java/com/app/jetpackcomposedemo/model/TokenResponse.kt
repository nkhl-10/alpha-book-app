package com.app.jetpackcomposedemo.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerializedName("token") val token: String? = "",   // Auth token
    @SerializedName("username") val username: String? = "", // Username
    @SerializedName("error") val error: String? = "",  // Error message (if login fails)
    @SerializedName("detail") val detail: String? = ""  // Error message (if login fails)
)
