package com.app.alpha_book.remote.api

import com.app.alpha_book.ui.utils.BASE_URL
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.util.InternalAPI




abstract class BaseApiService {
    protected val client = KtorClient.client

    protected suspend inline fun  getRequest(url: String): HttpResponse {
        return client.get(BASE_URL + url)
    }

    protected suspend inline fun <reified T : Any> postRequest(
        url: String,
        bodyData: T
    ): HttpResponse {
        return client.post(BASE_URL + url) {
            setBody(bodyData)
        }
    }
}