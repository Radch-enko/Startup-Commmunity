package com.multi.producthunt.domain.repository.implementation

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import com.multi.producthunt.domain.model.StartupDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.service.ProductHuntService
import com.multi.producthunt.network.util.CommonFlow
import com.multi.producthunt.network.util.asCommonFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class StartupsRepositoryImpl(private val productHuntService: ProductHuntService) :
    StartupsRepository {

    private val scope = MainScope()
    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    private val startupsPagingData: CommonFlow<PagingData<StartupDomain>>
        get() = startupsPager.pagingData.cachedIn(
            scope
        ).asCommonFlow()

    private val startupsPager = Pager(
        clientScope = scope,
        config = pagingConfig,
        initialKey = "",
        getItems = { currentKey, _ ->
            val startupsResponse = productHuntService.getStartups(currentKey)
            val items = startupsResponse.edges.map { edge -> edge.node.toDomain() }
            PagingResult(
                items,
                currentKey,
                prevKey = { startupsResponse.pageInfoResponse.pageInfo.startCursor },
                nextKey = { startupsResponse.pageInfoResponse.pageInfo.endCursor })
        })

    override fun getStartupsPagingData(): CommonFlow<PagingData<StartupDomain>> {
        return startupsPagingData
    }
}
