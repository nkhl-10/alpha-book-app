package com.app.alpha_book.remote.api

import com.app.alpha_book.ui.utils.BASE_URL
import io.ktor.client.request.get
import io.ktor.client.request.post

abstract class BaseApiService {
    protected val client = KtorClient.client
    protected suspend inline fun <reified T> getRequest(url: String): T {
        return client.get(BASE_URL + url)
    }


    protected suspend inline fun <reified T : Any, reified R> postRequest(
        url: String,
        bodyData: T
    ): R {
        return client.post(BASE_URL + url) {
            body = bodyData
        }

    }

}