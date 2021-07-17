package com.ht117.data.source

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

object RemoteConfig {
    const val BaseHost = "https://api.themoviedb.org/3"

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

    private external fun getAppId(): String

    fun client() = HttpClient(OkHttp) {
        followRedirects = true
        engine {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(logger)

            addInterceptor {
                val newUrl = it.request().url
                    .newBuilder()
                    .addQueryParameter("api_key", getAppId())
                    .build()
                val newReq = it.request().newBuilder()
                    .url(newUrl)
                    .build()
                it.proceed(newReq)
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }
}