package com.multi.producthunt.network.client

import com.multi.producthunt.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object SearchClient {
    val client = HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        defaultRequest {
            url(BuildKonfig.ALGOLIA_SEARCH_URL)
            headers {
                append("X-Algolia-API-Key", BuildKonfig.ALGOLIA_TOKEN)
                append("X-Algolia-Application-Id", BuildKonfig.ALGOLIA_APP_ID)
            }
        }
    }
}