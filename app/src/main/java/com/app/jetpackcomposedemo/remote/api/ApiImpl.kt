package com.app.jetpackcomposedemo.remote.api

import com.app.jetpackcomposedemo.model.LoginCredentials
import com.app.jetpackcomposedemo.model.User
import com.app.jetpackcomposedemo.ui.utils.BASE_URL
import com.app.jetpackcomposedemo.ui.utils.NOTES_URL
import com.app.jetpackcomposedemo.ui.utils.USER_URL
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiImpl : BaseApiService(), ApiInterface {
    /* private val api = KtorClient.client
    override suspend fun getUser(userId: Int): User {
        return api.get(USER_URL + userId)
    }
    override suspend fun registerUser(newUser: User): HttpResponse {
        return api.post(USER_URL) {
            contentType(ContentType.Application.Json)
            body = newUser
        }
    }
    override suspend fun loginUser(cred: LoginCredentials): User {
        return api.get {
            url(USER_URL)
            parameter("email", cred.email)
            parameter("password", cred.password)
        }
    }
    override suspend fun getTODOs(userId: String): String {
        *//*  return api.post {
            url(TODO_URL)
//            parameter("userId",userId)
        }*//*
        return api.get(NOTES_URL)
    }*/


    override suspend fun getUser(userId: Int): User {
        return getRequest(USER_URL)
    }

    override suspend fun registerUser(newUser: User): HttpResponse {
        return postRequest(USER_URL, newUser)
    }

    override suspend fun loginUser(cred: LoginCredentials): User {
        return KtorClient.client.get {
            url(BASE_URL+USER_URL)
            parameter("email", cred.email)
            parameter("password", cred.password)
        }
    }

    override suspend fun getTODOs(userId: String): String {
        return
    }
}