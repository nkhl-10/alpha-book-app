package com.app.jetpackcomposedemo.remote.api

import com.app.jetpackcomposedemo.model.ApiResponse
import com.app.jetpackcomposedemo.model.Book
import com.app.jetpackcomposedemo.model.BookImage
import com.app.jetpackcomposedemo.model.LoginRequest
import com.app.jetpackcomposedemo.model.TokenResponse
import com.app.jetpackcomposedemo.model.Transaction
import com.app.jetpackcomposedemo.model.User

interface ApiInterface {
    suspend fun register(user: User): ApiResponse<User>
    suspend fun login(credentials: LoginRequest): ApiResponse<TokenResponse>
    suspend fun getUsers(): List<User>
    suspend fun getBooks(): List<Book>
    suspend fun getTransactions(): List<Transaction>
    suspend fun getBookImages(): List<BookImage>
}
