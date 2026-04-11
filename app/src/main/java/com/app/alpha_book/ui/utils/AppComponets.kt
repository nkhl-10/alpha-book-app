package com.app.alpha_book.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//const val BASE_URL = "http://192.168.89.172:8000/api/"
const val BASE_URL = "https://alpha-book-backend.onrender.com/api/"

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
const val TRANSACTION_CONFIRM_URL = "transactionConfirm/"

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
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {}
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoBookAvailable() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.align(Alignment.Center)
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(6.dp))
                    .size(36.dp)
                    .padding(2.dp), onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.Info, contentDescription = "info")
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "No Books Data Available"
            )
        }
    }
}

@Composable
fun FullScreenLoadingDialog() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)) // translucent overlay
            .clickable(enabled = false) {},            // block user touch
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}
