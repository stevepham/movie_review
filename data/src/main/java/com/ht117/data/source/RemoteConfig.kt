package com.ht117.data.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

object RemoteConfig {
    const val BaseHost = "https://api.themoviedb.org/3"

    private val jsonConfig = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
        encodeDefaults = true
        classDiscriminator = "#class"
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
        install(ContentNegotiation) {
            json(jsonConfig)
        }
    }
}