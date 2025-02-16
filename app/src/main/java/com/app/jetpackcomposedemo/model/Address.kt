package com.app.jetpackcomposedemo.model

data class Address(
    val id: Int,
    val userId: Int,
    val street: String?,
    val city: String,
    val state: String,
    val zipCode: String?,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: Long
)
