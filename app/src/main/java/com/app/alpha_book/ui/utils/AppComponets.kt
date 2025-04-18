package com.app.alpha_book.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

const val BASE_URL = "http://192.168.90.172:8000/api/"
const val REGISTER_URL = "register"
const val LOGIN_URL = "login"
const val BOOKS_SEARCH_URL = "search"
const val BUY_BOOK_URL = "buyBook"
const val BOOKS_URL = "getBooks"
const val UPLOAD_BOOKS_URL = "books"
const val USER_URL = "users"
const val CATEGORIES_URL = "categories"
const val USER_BY_BOOKS_URL = "userByBooks"
const val TRANSACTION_URL = "transactions"
const val SOLD_BY_USER_BOOK = "soldByUserBook"
const val USER_BY_ORDERED_BOOKS_URL = "userByOrderedBooks"
const val UPLOAD_AVATAR_URL = "uploadAvatar/"
const val ADDRESS_URL = "addresses/"
const val ADDRESS_UPDATE_URL = "addressesUpdate"
const val ADDRESS_DELETE_URL = "addressesDelete"
const val SEARCH_CATEGORY_URL = "searchCategories"

enum class ApiStatus(val code: Int, val message: String) {
    // ✅ Success Codes
    SUCCESS(200, "Request successful"),
    CREATED(201, "Resource created successfully"),

    // ❌ Client Errors
    BAD_REQUEST(400, "Invalid request"),
    UNAUTHORIZED(401, "Unauthorized access"),
    FORBIDDEN(403, "Forbidden request"),
    NOT_FOUND(404, "Resource not found"),

    // ⚠️ Server Errors
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    BAD_GATEWAY(502, "Bad gateway"),
    SERVICE_UNAVAILABLE(503, "Service unavailable");

    // Function to get the message based on the status code
    companion object {
        fun fromCode(code: Int): ApiStatus {
            return values().find { it.code == code } ?: INTERNAL_SERVER_ERROR
        }
    }
}

@Composable
fun CenterLoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}
