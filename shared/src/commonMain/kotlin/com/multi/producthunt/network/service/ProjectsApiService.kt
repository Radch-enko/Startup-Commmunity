package com.multi.producthunt.network.service

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.AddProjectBody
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.body.RegisterBody
import com.multi.producthunt.network.model.body.UpdateUserBody
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.ProjectResponse
import com.multi.producthunt.network.model.response.TopicResponse
import com.multi.producthunt.network.model.response.UserResponse
import de.jensklingenberg.ktorfit.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.flow.Flow

interface ProjectsApiService {

    @Headers(["Content-Type: application/json"])
    @POST("users/login")
    fun login(@Body body: LoginBody): Flow<ApiResult<LoginResponse>>

    @Headers(["Content-Type: application/json"])
    @POST("users/create")
    fun register(@Body body: RegisterBody): Flow<ApiResult<UserResponse>>

    @Headers(["Content-Type: application/json"])
    @GET("users/me")
    fun me(@Header("Authorization") token: String): Flow<ApiResult<UserResponse>>

    @Headers(["Content-Type: application/json"])
    @POST("users/update")
    fun updateUser(
        @Body body: UpdateUserBody,
        @Header("Authorization") token: String
    ): Flow<ApiResult<UserResponse>>

    @Multipart
    @POST("users/profileImage")
    fun updateProfileImage(
        @Part profileImage: List<PartData>,
        @Header("Authorization") token: String
    ): Flow<ApiResult<UserResponse>>

    @Multipart
    @POST("users/coverImage")
    fun updateCoverImage(
        @Part coverImage: List<PartData>,
        @Header("Authorization") token: String
    ): Flow<ApiResult<UserResponse>>

    @Multipart
    @POST("projects/create")
    fun addProject(
        @Body body: AddProjectBody,
        @Header("Authorization") token: String
    ): Flow<ApiResult<ProjectResponse>>

    @GET("topics")
    fun getTopics(): Flow<ApiResult<List<TopicResponse>>>
}