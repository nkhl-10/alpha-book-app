package com.app.alpha_book.remote.api

import com.app.alpha_book.ui.utils.BASE_URL
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.content.PartData

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

    protected suspend inline fun <reified T : Any> putRequest(
        url: String,
        bodyData: T
    ): HttpResponse {
        return client.put(BASE_URL + url) {
            setBody(bodyData)
        }
    }

    protected suspend fun deleteRequest(url: String): HttpResponse {
        return client.delete(BASE_URL + url)
    }


    protected suspend fun postMultipartRequest(
        url: String,
        formData: List<PartData>
    ): HttpResponse {
        return client.post(BASE_URL + url) {
            setBody(MultiPartFormDataContent(formData))
        }
    }

}