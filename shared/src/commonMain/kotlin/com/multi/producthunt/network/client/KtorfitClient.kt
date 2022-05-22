package com.multi.producthunt.network.client

import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.network.service.ResultConverter
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.adapter.FlowResponseConverter
import de.jensklingenberg.ktorfit.create
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorfitClient {

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
    }

    private val ktorfit =
        Ktorfit(baseUrl = "https://product-hunt-projects.herokuapp.com/", client).also {
            it.addResponseConverter(ResultConverter())
        }

    val service = ktorfit.create<ProjectsApiService>()
}