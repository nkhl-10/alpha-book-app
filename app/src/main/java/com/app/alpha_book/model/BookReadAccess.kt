package com.app.alpha_book.model

data class BookReadAccess(
    val id: Int,
    val userId: Int,
    val bookId: Int,
    val accessGranted: Boolean,
    val paymentId: Int?,
    val accessedAt: Long
)
