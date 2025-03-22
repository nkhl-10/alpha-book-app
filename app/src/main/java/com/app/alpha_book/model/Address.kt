package com.app.alpha_book.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id: Int?=null,
    val user: Int,
    val street: String?,
    val city: String,
    val state: String,
    val zip_code: String?,
    val latitude: Double?,
    val longitude: Double?= null,
    val createdAt: Long?= null,
)
