package startup.community.shared.network.client

import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.network.service.ResultConverter
import startup.community.shared.utils.KMMPreference
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import startup.community.shared.BuildKonfig
import startup.community.shared.domain.usecase.AuthorizationUseCase

actual class KtorfitClient actual constructor(kmmPreference: KMMPreference) {

    private val client = HttpClient(OkHttp) {
        engine {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logger)

            addInterceptor { chain ->
                val token = kmmPreference.getString(AuthorizationUseCase.ACCESS_TOKEN)
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader(
                    "Content-Type",
                    "application/json;charset=UTF-8"
                )
                if (!token.isNullOrEmpty()) {
                    newRequest.addHeader(
                        "Authorization",
                        "Bearer $token"
                    )
                }
                chain.proceed(newRequest.build())
            }
        }
        install(ContentNegotiation) {
            json(
                Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = true}
            )
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