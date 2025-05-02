package com.app.alpha_book.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Int,
    val book: Book,
    val buyer: User,
    val amount: Double,
    val status: String,
    @SerialName("transaction_date") val transactionDate: String,
    val otp :Int
)

@Serializable
data class TransactionConfirm(
    @SerialName("transaction_id") val transactionId: Int?= null,
    @SerialName("otp") val otp: Int?= null
)