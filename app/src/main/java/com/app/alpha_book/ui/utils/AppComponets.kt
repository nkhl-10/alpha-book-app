package com.app.alpha_book.ui.utils


//const val BASE_URL ="https://67698771863eaa5ac0dbf523.mockapi.io/"
const val BASE_URL ="http://192.168.17.172:8000/api/"
const val REGISTER_URL = "register"
const val LOGIN_URL = "login"
const val BOOKS_SEARCH_URL = "search"
const val BUY_BOOK_URL= "buyBook"
const val BOOKS_URL = "getBooks"
const val USER_URL = "users"
const val CATEGORIES_URL = "categories"
const val USER_BY_BOOKS_URL = "userByBooks"
const val USER_BY_ORDERED_BOOKS_URL = "userByOrderedBooks"
const val UPLOAD_AVATAR_URL = "uploadAvatar"
const val ADDRESS_URL = "addresses"

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
