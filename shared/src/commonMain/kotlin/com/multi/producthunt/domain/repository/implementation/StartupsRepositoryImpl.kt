package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.*
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.SearchableStartupsResponse
import com.multi.producthunt.network.model.body.AddProjectBody
import com.multi.producthunt.network.model.body.TopicBody
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.ProjectResponse
import com.multi.producthunt.network.model.toDomain
import com.multi.producthunt.network.service.ProductHuntService
import com.multi.producthunt.network.service.ProjectsApiService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class StartupsRepositoryImpl(
    private val productHuntService: ProductHuntService,
    private val client: HttpClient,
    private val service: ProjectsApiService
) :
    StartupsRepository {
    override suspend fun getTopStartups(after: String): PostsDomain {
        return productHuntService.getStartups(after).posts.toDomain()
    }

    override suspend fun getSearchableStartups(
        query: String,
        page: Int
    ): SearchableStartupsDomain {
        val response = (client.get("Post_production") {
            parameter("query", query)
            parameter("page", page)
        }.body() as SearchableStartupsResponse)

        return SearchableStartupsDomain(
            response.startups.map { hitNetwork -> hitNetwork.toDomain() },
            response.pages,
            response.currentPage
        )
    }

    override fun addProject(
        token: String?,
        name: String,
        tagline: String,
        description: String,
        thumbnail: String?,
        media: List<String?>,
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>> {
        return service.addProject(AddProjectBody(
            name = name,
            tagline = tagline,
            description = description,
            thumbnail = thumbnail,
            media = media.filterNotNull(),
            topics = topics.map { TopicBody(it) }
        ), "Bearer " + token.orEmpty()).toDomain()
    }
}

private fun Flow<ApiResult<ProjectResponse>>.toDomain(): Flow<ApiResult<ProjectDomain>> {
    return this.map { response ->
        response.map { data: ProjectResponse ->
            ProjectDomain(
                id = data.id,
                name = data.name,
                tagline = data.tagline,
                thumbnail = data.thumbnail,
                media = data.media,
                createdDate = data.createdDate,
                isVoted = data.isVoted,
                topics = data.topics.map { TopicDomain(it.id, it.name) },
                voters = data.voters.map {
                    UserDomain(
                        name = it.name,
                        username = it.username,
                        headline = it.headline,
                        profileImage = it.profileImage,
                        coverImage = it.coverImage
                    )
                },
                votesCount = data.votesCount
            )
        }
    }
}
