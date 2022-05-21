package com.multi.producthunt.network.service

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.response.LoginResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.coroutines.flow.Flow

interface ProjectsApiService {

    @Headers(["Content-Type: application/json"])
    @POST("users/login")
    fun login(@Body body: LoginBody): Flow<ApiResult<LoginResponse>>

//    @POST("/users/create")
//    fun register(@Body body: RegisterBody): Flow<RegisterResponse>
}