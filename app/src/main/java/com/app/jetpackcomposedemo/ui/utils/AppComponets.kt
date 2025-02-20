package com.app.jetpackcomposedemo.ui.utils


//const val BASE_URL ="https://67698771863eaa5ac0dbf523.mockapi.io/"
const val BASE_URL ="http://192.168.171.172:8000/api/"
const val REGISTER_URL = "register"
const val LOGIN_URL = "login"
const val BOOKS_URL = "books"


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
