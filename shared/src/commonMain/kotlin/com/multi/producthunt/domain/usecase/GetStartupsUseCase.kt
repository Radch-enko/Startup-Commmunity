package com.multi.producthunt.domain.usecase

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.SuccessResponse
import com.multi.producthunt.network.util.asCommonFlow
import com.multi.producthunt.ui.models.ProjectUI
import com.multi.producthunt.ui.models.toUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.datetime.LocalDate

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class GetStartupsUseCase(
    private val repository: StartupsRepository
) {

    private val scope = MainScope()
    private val pagingConfig = PagingConfig(
        pageSize = 50,
        enablePlaceholders = false,
        initialLoadSize = 150,
        prefetchDistance = 150
    )

    fun getStartupsPagingData(
        date: LocalDate?, makerId: Int? = null, topicId: Int? = null, searchQuery: String? = null
    ): Flow<PagingData<ProjectUI>> {
        return Pager(
            clientScope = scope,
            config = pagingConfig,
            initialKey = 0,
            getItems = { currentKey, size ->

                val day =
                    if (date != null) "${date.year}-${date.monthNumber}-${date.dayOfMonth}" else null

                val items =
                    repository.getProjects(
                        cursor = currentKey,
                        pageSize = size,
                        day = day,
                        makerId = makerId,
                        topicId= topicId,
                        searchQuery = searchQuery
                    )
                        .single().map { projectsDomains ->
                            projectsDomains.map { domain ->
                                domain.toUI()
                            }
                        }.data!!

                PagingResult(
                    items = items,
                    currentKey = currentKey,
                    prevKey = { if (currentKey != 0) currentKey - size else 0 }, // Key for previous page, null means don't load previous pages
                    nextKey = { currentKey + size } // Key for next page. Use `items` or `currentKey` to get it depending on the pagination strategy
                )
            }).pagingData.asCommonFlow().cachedIn(scope)
    }

    fun voteProject(projectId: Int): Flow<ApiResult<SuccessResponse>> {
        return repository.voteProject(projectId)
    }
}
