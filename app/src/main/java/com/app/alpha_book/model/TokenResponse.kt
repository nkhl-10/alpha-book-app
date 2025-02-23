package com.app.alpha_book.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerializedName("token") val token: String? = "",   // Auth token
    @SerializedName("username") val username: String? = "", // Username
    @SerializedName("id") val id: Int? = 0, // Id
    @SerializedName("error") val error: String? = "",  // Error message (if login fails)
    @SerializedName("detail") val detail: String? = ""  // Error message (if login fails)
)
