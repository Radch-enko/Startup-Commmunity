package com.multi.producthunt.network.service

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.AddProjectBody
import com.multi.producthunt.network.model.body.CreateCommentBody
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.body.RegisterBody
import com.multi.producthunt.network.model.body.UpdateUserBody
import com.multi.producthunt.network.model.response.DetailProjectResponse
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.ProjectResponse
import com.multi.producthunt.network.model.response.SuccessResponse
import com.multi.producthunt.network.model.response.TopicResponse
import com.multi.producthunt.network.model.response.UserResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.QueryMap
import kotlinx.coroutines.flow.Flow

interface ProjectsApiService {

    // ProjectsFlow
    @POST("projects/create")
    fun addProject(
        @Body body: AddProjectBody
    ): Flow<ApiResult<ProjectResponse>>

    @GET("projects")
    fun getProjects(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int
    ): Flow<ApiResult<List<ProjectResponse>>>

    @GET("projects/{project_id}")
    fun getProjectById(
        @Path("project_id") projectId: Int
    ): Flow<ApiResult<DetailProjectResponse>>

    @POST("projects/{project_id}/update")
    fun updateProject(
        @Path("project_id") projectId: Int,
        @Body body: AddProjectBody
    ): Flow<ApiResult<ProjectResponse>>

    @POST("projects/{project_id}/comment")
    fun commentForProject(
        @Body body: CreateCommentBody
    ): Flow<ApiResult<DetailProjectResponse>>

    @GET("projects")
    fun getProjectsByDay(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("day") day: String
    ): Flow<ApiResult<List<ProjectResponse>>>

    @GET("projects")
    fun getMakerProjects(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("makerId") makerId: Int
    ): Flow<ApiResult<List<ProjectResponse>>>

    @GET("projects")
    fun getProjectsByTopicId(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("topicId") topicId: Int
    ): Flow<ApiResult<List<ProjectResponse>>>

    @GET("projects")
    fun searchProjects(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @QueryMap(true) map : Map<String,String>
    ): Flow<ApiResult<List<ProjectResponse>>>

    @GET("projects/{project_id}/vote")
    fun voteForProject(
        @Path("project_id") projectId: Int,
    ): Flow<ApiResult<SuccessResponse>>

    @POST("projects/{project_id}/delete")
    fun deleteProject(
        @Path("project_id") projectId: Int,
    ): Flow<ApiResult<SuccessResponse>>

    // UsersFlow
    @POST("users/login")
    fun login(@Body body: LoginBody): Flow<ApiResult<LoginResponse>>

    @POST("users/create")
    fun register(@Body body: RegisterBody): Flow<ApiResult<UserResponse>>

    @GET("users/me")
    fun me(): Flow<ApiResult<UserResponse>>

    @POST("users/update")
    fun updateUser(
        @Body body: UpdateUserBody
    ): Flow<ApiResult<UserResponse>>

    @GET("users")
    fun getAllUsers(
        @Query("cursor") cursor: Int,
        @Query("page_size") pageSize: Int,
        @Query("searchQuery") searchQuery: String,
    ): Flow<ApiResult<List<UserResponse>>>

    @GET("users/{user_id}")
    fun getUserById(
        @Path("user_id") id: Int
    ): Flow<ApiResult<UserResponse>>

    // Topics flow
    @GET("topics")
    fun getTopics(): Flow<ApiResult<List<TopicResponse>>>
}