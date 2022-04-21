package com.multi.producthunt.domain.usecase

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import com.multi.producthunt.domain.model.toStartupUI
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.util.asCommonFlow
import com.multi.producthunt.ui.models.StartupUI
import com.multi.producthunt.ui.models.toUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class GetStartupsUseCase(private val repository: StartupsRepository) {

    private val scope = MainScope()
    private val pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = true)

    fun getStartupsPagingData(type: StartupsRequestType): Flow<PagingData<StartupUI>> {
        return Pager(
            clientScope = scope,
            config = pagingConfig,
            initialKey = "",
            getItems = { currentKey, _ ->
                val startupsResponse = when (type) {
                    StartupsRequestType.TOP -> repository.getTopStartups(currentKey)
                    StartupsRequestType.TIMELINE -> TODO("Setup request for TIMELINE feature")
                }
                val items =
                    startupsResponse.list.map { startupDomain -> startupDomain.toUI() }

                PagingResult(
                    items,
                    currentKey,
                    prevKey = { startupsResponse.start },
                    nextKey = { startupsResponse.end })
            }).pagingData.asCommonFlow().cachedIn(scope)
    }

    fun getSearchableStartupsPagingData(query: String): Flow<PagingData<StartupUI>> {
        return Pager(
            clientScope = scope,
            config = pagingConfig,
            initialKey = 0,
            getItems = { currentKey, _ ->
                val response = repository.getSearchableStartups(query, currentKey)

                val items = response.startups.map { startupDomain -> startupDomain.toStartupUI() }

                PagingResult(
                    items,
                    currentKey,
                    prevKey = { currentKey - 1 },
                    nextKey = { currentKey + 1 })
            }).pagingData.asCommonFlow().cachedIn(scope)
    }
}