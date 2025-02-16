package com.app.jetpackcomposedemo.remote.api

import android.util.Log
import com.app.jetpackcomposedemo.model.ApiResponse
import com.app.jetpackcomposedemo.model.Book
import com.app.jetpackcomposedemo.model.BookImage
import com.app.jetpackcomposedemo.model.LoginRequest
import com.app.jetpackcomposedemo.model.TokenResponse
import com.app.jetpackcomposedemo.model.Transaction
import com.app.jetpackcomposedemo.model.User
import com.app.jetpackcomposedemo.ui.utils.ApiStatus
import com.app.jetpackcomposedemo.ui.utils.LOGIN_URL
import com.app.jetpackcomposedemo.ui.utils.REGISTER_URL
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import kotlinx.serialization.json.Json
import io.ktor.client.statement.*
import kotlinx.serialization.SerializationException

class ApiImpl : BaseApiService(), ApiInterface {

    override suspend fun register(user: User): ApiResponse<User> {
        /*val response: HttpResponse = postRequest(REGISTER_URL, user)
        val statusCode = response.status.value
        val responseBody: String = response.receive()
        val apiResponse = Json.decodeFromString<ApiResponse<User>>(responseBody)
        return apiResponse.copy(status = statusCode) // Attach status code*/
        return safeApiCall {
            postRequest(REGISTER_URL, user) // ✅ Reuse API function
        }
    }

    override suspend fun login(credentials: LoginRequest):ApiResponse<TokenResponse>  {
       /* val response: HttpResponse = postRequest(LOGIN_URL, credentials)
        val statusCode = response.status.value
        val responseBody: String = response.receive()
        val apiResponse = Json.decodeFromString<ApiResponse<TokenResponse>>(responseBody)
        return apiResponse.copy(status = statusCode) // Attach status code*/
        return safeApiCall {
            postRequest(LOGIN_URL, credentials)
        }
    }

    override suspend fun getUsers(): List<User> {
        return getRequest("users")
    }

    override suspend fun getBooks(): List<Book> {
        return getRequest("books")
    }

    override suspend fun getTransactions(): List<Transaction> {
        return getRequest("transactions")
    }

    override suspend fun getBookImages(): List<BookImage> {
        return getRequest("book_images")
    }
}



suspend inline fun <reified T> safeApiCall(apiCall: () -> HttpResponse): ApiResponse<T> {
    return try {
        val response: HttpResponse = apiCall() // ✅ Execute API call
        val statusCode = response.status.value // ✅ Extract status code
        val responseBody: String = response.receive() // ✅ Read response as text

        Log.i("TAG", "Response Status: $statusCode")
        Log.i("TAG", "Raw Response: $responseBody")

        return try {
            // ✅ Ensure JSON response structure is correct before parsing
            val apiResponse = Json.decodeFromString<ApiResponse<T>>(responseBody)
            apiResponse.copy(status = statusCode)
        } catch (e: SerializationException) {
            Log.e("TAG", "Serialization Error: ${e.message}")
            ApiResponse(status = statusCode, message = responseBody, data = null)
        }

    } catch (e: ClientRequestException) { // ✅ Handles 400 Bad Request
        val statusCode = e.response.status.value // ✅ Extract correct status code
        val errorBody = e.response.readText() // ✅ Extract error message

        Log.e("TAG", "ClientRequestException: Status $statusCode, Error: $errorBody")

        ApiResponse(status = statusCode, message = errorBody, data = null)

    } catch (e: Exception) { // ✅ Handles all other errors (Network, Timeout, etc.)
        Log.e("TAG", "Network Error: ${e.message}")
        ApiResponse(status = 500, message = "Network Error: ${e.message}", data = null)
    }
}
