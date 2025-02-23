package com.app.jetpackcomposedemo.remote.api

import com.app.jetpackcomposedemo.model.ApiResponse
import com.app.jetpackcomposedemo.model.Book
import com.app.jetpackcomposedemo.model.Category
import com.app.jetpackcomposedemo.model.LoginRequest
import com.app.jetpackcomposedemo.model.TokenResponse
import com.app.jetpackcomposedemo.model.Transaction
import com.app.jetpackcomposedemo.model.User

interface ApiInterface {
    suspend fun register(user: User): ApiResponse<User>

    suspend fun login(credentials: LoginRequest): ApiResponse<TokenResponse>

    suspend fun getBooks(): ApiResponse<List<Book>>

    suspend fun getBooks(bookId: Int): ApiResponse<Book>

    suspend fun searchBooks(query: String): ApiResponse<List<Book>>

    suspend fun buyBook(bookId: Int): ApiResponse<Book>

    suspend fun getCategories(): ApiResponse<List<Category>>

    suspend fun getCategoriesByBooks(categoryId: Int): ApiResponse<List<Book>>

    suspend fun getUserByBooks(bookId: Int): ApiResponse<List<Book>>

    suspend fun getUserByOrderedBooks(userId: Int): ApiResponse<List<Book>>

}
