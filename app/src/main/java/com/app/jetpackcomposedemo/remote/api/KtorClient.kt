package com.app.jetpackcomposedemo.remote.api

import android.content.Context
import com.app.jetpackcomposedemo.remote.sharedPreferences.USER
import com.app.jetpackcomposedemo.remote.sharedPreferences.getStringData
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

val json = Json {
    encodeDefaults= true
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
}

object KtorClient {
    val client = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
        install(DefaultRequest) {
            headers.append("Content-Type", "application/json")
        }
    }
}