package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.PostsDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.service.ProductHuntService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class StartupsRepositoryImpl(private val productHuntService: ProductHuntService) :
    StartupsRepository {
    override suspend fun getTopStartups(after: String): PostsDomain {
        return productHuntService.getStartups(after).posts.toDomain()
    }
}
