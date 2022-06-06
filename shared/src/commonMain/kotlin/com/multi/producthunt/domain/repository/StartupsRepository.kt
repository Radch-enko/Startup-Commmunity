package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.ProjectDomain
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.VoteResponse
import kotlinx.coroutines.flow.Flow

interface StartupsRepository {
    fun addProject(
        name: String,
        tagline: String,
        description: String,
        ownerLink: String,
        thumbnail: String? = null,
        media: List<String?> = emptyList(),
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>>

    fun updateProject(
        projectId: Int,
        name: String,
        tagline: String,
        description: String,
        ownerLink: String,
        thumbnail: String? = null,
        media: List<String?> = emptyList(),
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>>

    fun getProjects(
        cursor: Int,
        pageSize: Int?,
        day: String?,
        makerId: Int?
    ): Flow<ApiResult<List<ProjectDomain>>>

    fun getProjectById(projectId: Int): Flow<ApiResult<ProjectDomain>>

    fun commentForProject(projectId: Int, text: String): Flow<ApiResult<ProjectDomain>>

    fun voteProject(
        projectId: Int
    ): Flow<ApiResult<VoteResponse>>
}