package com.app.alpha_book.model

import kotlinx.serialization.Serializable

@Serializable
data class BuyReqModel(
    val buyerId: Int, val bookId: Int, val amount: Double
)
