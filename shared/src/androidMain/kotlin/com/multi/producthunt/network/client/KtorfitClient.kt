package com.multi.producthunt.network.client

import com.multi.producthunt.BuildKonfig
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.network.service.ResultConverter
import com.multi.producthunt.utils.KMMPreference
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

actual class KtorfitClient actual constructor(kmmPreference: KMMPreference) {

    val token = kmmPreference.getString(AuthorizationUseCase.ACCESS_TOKEN)

    private val client = HttpClient(OkHttp) {
        engine {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logger)
        }
        if (!token.isNullOrEmpty()) {
            install(Auth) {
                bearer {
                    loadTokens {
                        // Load tokens from a local storage and return them as the 'BearerTokens' instance
                        BearerTokens(
                            kmmPreference.getString(AuthorizationUseCase.ACCESS_TOKEN) ?: "",
                            "xyz111"
                        )
                    }
                }
            }
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