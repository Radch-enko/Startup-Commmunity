package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.ProjectDomain
import com.multi.producthunt.network.model.ApiResult
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
        token: String?
    ): Flow<ApiResult<List<ProjectDomain>>>
}