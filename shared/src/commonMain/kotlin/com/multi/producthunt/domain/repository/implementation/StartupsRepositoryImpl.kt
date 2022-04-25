package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.PostsDomain
import com.multi.producthunt.domain.model.SearchableStartupsDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.model.SearchableStartupsResponse
import com.multi.producthunt.network.model.toDomain
import com.multi.producthunt.network.service.ProductHuntService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class StartupsRepositoryImpl(
    private val productHuntService: ProductHuntService,
    private val client: HttpClient
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
}