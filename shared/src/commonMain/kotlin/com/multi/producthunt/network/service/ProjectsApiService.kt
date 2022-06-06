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
import com.multi.producthunt.network.model.response.VoteResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow

interface ProjectsApiService {

    // ProjectsFlow
    @Headers(["Content-Type: application/json"])
    @POST("projects/create")
    fun addProject(
        @Body body: AddProjectBody
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @GET("projects")
    fun getProjects(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int
    ): Flow<ApiResult<List<ProjectResponse>>>

    @Headers(["Content-Type: application/json"])
    @GET("projects/{project_id}")
    fun getProjectById(
        @Path("project_id") projectId: Int
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @POST("projects/{project_id}/update")
    fun updateProject(
        @Path("project_id") projectId: Int,
        @Body body: AddProjectBody
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @POST("projects/{project_id}/comment")
    fun commentForProject(
        @Body body: CreateCommentBody
    ): Flow<ApiResult<ProjectResponse>>

    @Headers(["Content-Type: application/json"])
    @GET("projects")
    fun getProjectsByDay(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("day") day: String
    ): Flow<ApiResult<List<ProjectResponse>>>

    @Headers(["Content-Type: application/json"])
    @GET("projects")
    fun getMakerProjects(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("makerId") makerId: Int
    ): Flow<ApiResult<List<ProjectResponse>>>

    @Headers(["Content-Type: application/json"])
    @GET("projects/{project_id}/vote")
    fun voteForProject(
        @Path("project_id") projectId: Int,
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
    fun me(): Flow<ApiResult<UserResponse>>

    @Headers(["Content-Type: application/json"])
    @POST("users/update")
    fun updateUser(
        @Body body: UpdateUserBody
    ): Flow<ApiResult<UserResponse>>

    @Headers(["Content-Type: application/json"])
    @GET("users/{user_id}")
    fun getUserById(
        @Path("user_id") id: Int
    ): Flow<ApiResult<UserResponse>>

    // Other
    @GET("topics")
    fun getTopics(): Flow<ApiResult<List<TopicResponse>>>
}