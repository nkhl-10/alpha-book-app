package com.app.alpha_book.remote.api

import android.util.Log
import com.app.alpha_book.model.ApiResponse
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.BuyReqModel
import com.app.alpha_book.model.Category
import com.app.alpha_book.model.LoginRequest
import com.app.alpha_book.model.TokenResponse
import com.app.alpha_book.model.User
import com.app.alpha_book.ui.utils.BASE_URL
import com.app.alpha_book.ui.utils.BOOKS_SEARCH_URL
import com.app.alpha_book.ui.utils.BOOKS_URL
import com.app.alpha_book.ui.utils.BUY_BOOK_URL
import com.app.alpha_book.ui.utils.CATEGORIES_URL
import com.app.alpha_book.ui.utils.LOGIN_URL
import com.app.alpha_book.ui.utils.REGISTER_URL
import com.app.alpha_book.ui.utils.UPLOAD_AVATAR_URL
import com.app.alpha_book.ui.utils.USER_BY_BOOKS_URL
import com.app.alpha_book.ui.utils.USER_BY_ORDERED_BOOKS_URL
import com.app.alpha_book.ui.utils.USER_URL
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import java.io.File

class ApiImpl : BaseApiService(), ApiInterface {

    override suspend fun register(user: User): ApiResponse<User> =
        safeApiCall { postRequest(REGISTER_URL, user) }

    override suspend fun login(credentials: LoginRequest): ApiResponse<TokenResponse> =
        safeApiCall { postRequest(LOGIN_URL, credentials) }

    override suspend fun buy(buyReqModel: BuyReqModel): ApiResponse<String> =
        safeApiCall { postRequest(BUY_BOOK_URL, buyReqModel) }

    override suspend fun getBook(): ApiResponse<List<Book>> = safeApiCall { getRequest(BOOKS_URL) }

    override suspend fun getBook(bookId: Int): ApiResponse<Book> {
        val response: HttpResponse = getRequest("$BOOKS_URL/$bookId")
        val statusCode = response.status.value
        val responseBody: String = response.bodyAsText()

        Log.i("API_CALL", "Response Status:$bookId :: $statusCode")
        Log.i("API_CALL", "Raw Response: $responseBody")

        return try {
            val book: Book = Json.decodeFromString(responseBody) // Deserialize correctly
            ApiResponse(status = statusCode, data = book, message = "Success")
        } catch (e: SerializationException) {
            Log.e("API_CALL", "Serialization Error: ${e.message}")
            ApiResponse(
                status = statusCode,
                data = null,
                message = "Serialization Error: ${e.message}"
            )
        }
    }

    override suspend fun getUser(userId: Int): ApiResponse<User> {
        val response: HttpResponse = getRequest("$USER_URL/$userId")
        Log.i("API_CALL", "Raw Response: $response")
        val statusCode = response.status.value
        Log.i("API_CALL", "Raw Response: $statusCode")
        val responseBody: String = response.bodyAsText()

        Log.i("API_CALL", "Response Status:$userId :: $statusCode")
        Log.i("API_CALL", "Raw Response: $responseBody")

        return try {
            val book: User = Json.decodeFromString(responseBody) // Deserialize correctly
            ApiResponse(status = statusCode, data = book, message = "Success")
        } catch (e: SerializationException) {
            Log.e("API_CALL", "Serialization Error: ${e.message}")
            ApiResponse(
                status = statusCode,
                data = null,
                message = "Serialization Error: ${e.message}"
            )
        }
    }

    override suspend fun uploadUserImage(userId: Int, file: File): ApiResponse<String> {
        val response: HttpResponse = KtorClient.client.post("$BASE_URL$UPLOAD_AVATAR_URL/") {
            contentType(ContentType.MultiPart.FormData)
            setBody(MultiPartFormDataContent(
                formData {
                    append("user_id", userId.toString())
                    append("avatar", file.readBytes(), Headers.build {
                        append(
                            HttpHeaders.ContentDisposition,
                            "form-data; name=\"avatar\"; filename=\"${file.name}\""
                        )
                    })
                }
            ))
        }
        return ApiResponse(
            status = response.status.value,
            data = null,
            message = "Upload Successful"
        )
    }


    override suspend fun searchBooks(query: String): ApiResponse<List<Book>> =
        safeApiCall { getRequest("$BOOKS_SEARCH_URL?q=$query") }

    override suspend fun buyBook(bookId: Int): ApiResponse<Book> = safeApiCall {
        getRequest("$BUY_BOOK_URL/$bookId")
    }

    override suspend fun getCategories(): ApiResponse<List<Category>> = safeApiCall {
        getRequest(CATEGORIES_URL)
    }

    override suspend fun getCategories(categoryId: Int): ApiResponse<List<Book>> = safeApiCall {
        getRequest("$CATEGORIES_URL/$categoryId")
    }

    override suspend fun getCategoriesByBooks(categoryId: Int): ApiResponse<List<Book>> =
        safeApiCall {
            getRequest("$CATEGORIES_URL/$categoryId")
        }

    override suspend fun getUserByBooks(bookId: Int): ApiResponse<List<Book>> = safeApiCall {
        getRequest("$USER_BY_BOOKS_URL/$bookId")
    }

    override suspend fun getUserByOrderedBooks(userId: Int): ApiResponse<List<Book>> = safeApiCall {
        getRequest("$USER_BY_ORDERED_BOOKS_URL/$userId")
    }
}


suspend inline fun <reified T> safeApiCall(apiCall: () -> HttpResponse): ApiResponse<T> {
    return try {
        val response: HttpResponse = apiCall()
        val statusCode = response.status.value
        val responseBody: String = response.bodyAsText()

        Log.i("safeApiCall", "Response Status: $statusCode")
        Log.i("safeApiCall", "Raw Response: $responseBody")

        return try {
            val jsonElement = json.parseToJsonElement(responseBody)

            val data: T? = when {
                jsonElement is JsonArray && T::class == List::class -> {
                    json.decodeFromString(responseBody)
                }

                jsonElement is JsonObject -> {
                    json.decodeFromString(responseBody)
                }

                else -> throw SerializationException("Unexpected JSON format")
            }

            Log.i("safeApiCall", "Serialization Success: $data")
            ApiResponse(status = statusCode, data = data, message = "Success")

        } catch (e: SerializationException) {
            Log.e("safeApiCall", "Serialization Error: ${e.message}")
            ApiResponse(
                status = statusCode,
                message = "Serialization Error: ${e.message}",
                data = null
            )
        }

    } catch (e: ClientRequestException) {
        val statusCode = e.response.status.value
        val errorBody = e.response.bodyAsText()
        Log.e("safeApiCall", "ClientRequestException: Status $statusCode, Error: $errorBody")
        ApiResponse(status = statusCode, message = errorBody, data = null)

    } catch (e: Exception) {
        Log.e("safeApiCall", "Network Error: ${e.message}")
        ApiResponse(status = 500, message = "Network Error: ${e.message}", data = null)
    }
}