package com.multi.producthunt.network.service

import com.multi.producthunt.network.model.ApiResult
import de.jensklingenberg.ktorfit.adapter.ResponseConverter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.platformType
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.flow

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
                            ApiResult.Error(exception = response.status.description)
                        }
                    }
                } catch (exception: Exception) {
                    ApiResult.Error(exception.message.toString())
                }
            )
        }
    }
}