package com.app.alpha_book.remote.api

import com.app.alpha_book.model.ApiResponse
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.Category
import com.app.alpha_book.model.LoginRequest
import com.app.alpha_book.model.TokenResponse
import com.app.alpha_book.model.User

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
