package com.app.jetpackcomposedemo.model

data class Transaction(
    val id: Int,
    val bookId: Int,
    val buyerId: Int,
    val amount: Double,
    val status: String,
    val transactionDate: Long
)
