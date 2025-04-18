package com.app.alpha_book.remote.api

import com.app.alpha_book.model.Address
import com.app.alpha_book.model.ApiResponse
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.BookUploadModel
import com.app.alpha_book.model.BuyReqModel
import com.app.alpha_book.model.Category
import com.app.alpha_book.model.LoginRequest
import com.app.alpha_book.model.TokenResponse
import com.app.alpha_book.model.User
import java.io.File

interface ApiInterface {
    suspend fun uploadBook(bookData: BookUploadModel): ApiResponse<String>

    suspend fun register(user: User): ApiResponse<User>

    suspend fun login(credentials: LoginRequest): ApiResponse<TokenResponse>

    suspend fun buy(buyReqModel: BuyReqModel): ApiResponse<String>

    suspend fun getBook(): ApiResponse<List<Book>>

    suspend fun getBook(bookId: Int): ApiResponse<Book>

    suspend fun getUser(userId: Int): ApiResponse<User>

    suspend fun searchBooks(query: String): ApiResponse<List<Book>>

    suspend fun buyBook(bookId: Int): ApiResponse<Book>

    suspend fun getCategories(): ApiResponse<List<Category>>

    suspend fun getCategories(categoryId: Int): ApiResponse<List<Book>>

    suspend fun getCategoriesByBooks(categoryId: Int): ApiResponse<List<Book>>

    suspend fun getUserByBooks(bookId: Int): ApiResponse<List<Book>>

    suspend fun getUserByOrderedBooks(userId: Int): ApiResponse<List<Book>>

    suspend fun uploadUserImage(userId: Int, file: File): ApiResponse<String>

    suspend fun addAddress(address: Address): ApiResponse<String>

    suspend fun editAddress(address: Address,addressId: Int): ApiResponse<String>

    suspend fun deleteAddress(addressId: Int): ApiResponse<String>

    suspend fun getAddress(userId: Int): ApiResponse<List<Address>>

    suspend fun searchCategories(query: String): ApiResponse<List<Category>>
}
