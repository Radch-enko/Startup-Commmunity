package com.multi.producthunt.network.service

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.ErrorResponse
import de.jensklingenberg.ktorfit.adapter.ResponseConverter
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KClass

class ResultConverter : ResponseConverter {

    override fun supportedType(returnTypeName: String): Boolean {
        return returnTypeName == "kotlinx.coroutines.flow.Flow"
    }

    override fun <PRequest : Any> wrapResponse(
        returnTypeName: String,
        requestFunction: suspend () -> Pair<TypeInfo, HttpResponse>
    ): Any {
        return flow {
            emit(
                try {
                    val (info, response) = requestFunction()
                    when (response.status) {
                        HttpStatusCode.OK -> {
                            val responseType = info.kotlinType?.arguments?.get(0)

                            val responseTypeInfo = TypeInfo(
                                responseType?.type?.classifier as KClass<*>,
                                responseType.type?.platformType!!, responseType.type
                            )

                            ApiResult.Success(
                                response.call.body(responseTypeInfo)
                            )
                        }
                        else -> {
                            val errorResponse: ErrorResponse? = response.call.body()

                            ApiResult.Error(
                                exception = errorResponse?.detail ?: response.status.description
                            )
                        }
                    }
                } catch (exception: Exception) {
                    Napier.e("ResultConverter", exception.cause)
                    ApiResult.Error("Something went wrong, please try again later")
                }
            )
        }
    }
}