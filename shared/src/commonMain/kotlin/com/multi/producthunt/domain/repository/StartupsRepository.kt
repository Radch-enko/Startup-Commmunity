package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.ProjectDomain
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.VoteResponse
import kotlinx.coroutines.flow.Flow

interface StartupsRepository {
    fun addProject(
        token: String?,
        name: String,
        tagline: String,
        description: String,
        thumbnail: String? = null,
        media: List<String?> = emptyList(),
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>>

    fun getProjects(
        cursor: Int,
        pageSize: Int?,
        day: String?,
        token: String?
    ): Flow<ApiResult<List<ProjectDomain>>>

    fun getProjectById(projectId: Int, token: String?): Flow<ApiResult<ProjectDomain>>

    fun commentForProject(projectId: Int, text: String, token: String?): Flow<ApiResult<ProjectDomain>>

    fun voteProject(
        projectId: Int,
        token: String?
    ): Flow<ApiResult<VoteResponse>>
}