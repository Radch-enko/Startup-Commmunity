package com.multi.producthunt.network.service

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.AddProjectBody
import com.multi.producthunt.network.model.body.CreateCommentBody
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.body.RegisterBody
import com.multi.producthunt.network.model.body.UpdateUserBody
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.ProjectResponse
import com.multi.producthunt.network.model.response.TopicResponse
import com.multi.producthunt.network.model.response.UserResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import com.multi.producthunt.network.model.response.*
import de.jensklingenberg.ktorfit.http.*
import kotlinx.coroutines.flow.Flow

interface ProjectsApiService {

    // ProjectsFlow
    @Headers(["Content-Type: application/json"])
    @POST("projects/create")
    fun addProject(
        @Body body: AddProjectBody,
        @Header("Authorization") token: String
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @GET("projects")
    fun getProjects(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Header("Authorization") token: String
    ): Flow<ApiResult<List<ProjectResponse>>>

    @Headers(["Content-Type: application/json"])
    @GET("projects/{project_id}")
    fun getProjectById(
        @Path("project_id") projectId: Int,
        @Header("Authorization") token: String
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @POST("projects/{project_id}/comment")
    fun commentForProject(
        @Body body: CreateCommentBody,
        @Header("Authorization") token: String
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @GET("projects")
    fun getProjectsByDay(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("day") day: String,
        @Header("Authorization") token: String
    ): Flow<ApiResult<List<ProjectResponse>>>

    @Headers(["Content-Type: application/json"])
    @GET("projects/{project_id}/vote")
    fun voteForProject(
        @Path("project_id") projectId: Int,
        @Header("Authorization") token: String
    ): Flow<ApiResult<VoteResponse>>

    // UsersFlow
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

    // Other
    @GET("topics")
    fun getTopics(): Flow<ApiResult<List<TopicResponse>>>
}