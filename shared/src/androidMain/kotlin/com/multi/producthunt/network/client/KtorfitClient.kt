package com.multi.producthunt.network.client

import com.multi.producthunt.BuildKonfig
import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.network.service.ResultConverter
import com.multi.producthunt.utils.KMMPreference
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

actual class KtorfitClient(private val kmmPreference: KMMPreference) {

    private val client = HttpClient(OkHttp) {
        engine {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logger)
        }
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = true })
        }
    }

    private val ktorfit =
        Ktorfit(baseUrl = BuildKonfig.BASE_URL, client).also {
            it.addResponseConverter(ResultConverter())
        }

    actual fun getService(): ProjectsApiService {
        return ktorfit.create()
    }
}