package com.app.jetpackcomposedemo.model

data class Book(
    val id: Int,
    val title: String,
    val author: String?,
    val description: String?,
    val categoryId: Int?,
    val sellerId: Int?,
    val price: Double?,
    val condition: String,
    val bookType: String,
    val pdfFile: String?,
    val readAccess: String?,
    val locationId: Int?,
    val createdAt: Long,
    val updatedAt: Long
)
