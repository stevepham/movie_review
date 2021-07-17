package com.ht117.data.source

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

private val Url.hostWithPortIfRequired: String
    get() = if (port == protocol.defaultPort) {
        host
    } else {
        hostWithPort
    }

private val Url.fullUrl: String
    get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun provideMockClient(map: Map<String, String>): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler { req ->
                if (map.containsKey(req.url.fullUrl)) {
                    respond(
                        map[req.url.fullUrl]!!,
                        HttpStatusCode.OK,
                        headersOf("Content-Type", ContentType.Application.Json.toString())
                    )
                } else {
                    error("Not found")
                }
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }
}