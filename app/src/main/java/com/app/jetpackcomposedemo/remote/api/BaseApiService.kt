package com.app.jetpackcomposedemo.remote.api

import com.app.jetpackcomposedemo.ui.utils.BASE_URL
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders

abstract class BaseApiService {
    protected val client = KtorClient.client
    protected suspend inline fun <reified T> getRequest(url: String): T {
        return client.get(BASE_URL + url){
            KtorClient.authToken?.let { token ->
                header(HttpHeaders.Authorization, "Token $token")
            }
        }
    }

    protected suspend inline fun <reified T : Any, reified R> postRequest(
        url: String,
        bodyData: T
    ): R {
        return client.post(BASE_URL + url) {
            KtorClient.authToken?.let { token ->
                header(HttpHeaders.Authorization, "Token $token")
            }
            body = bodyData
        }

    }
}