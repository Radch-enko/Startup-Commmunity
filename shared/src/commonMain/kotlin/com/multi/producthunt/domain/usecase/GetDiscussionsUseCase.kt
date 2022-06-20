package com.multi.producthunt.domain.usecase

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import com.multi.producthunt.domain.repository.DiscussionsRepository
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.util.asCommonFlow
import com.multi.producthunt.ui.models.DiscussionItemUI
import com.multi.producthunt.ui.models.toDiscussionItemUI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class GetDiscussionsUseCase(
    private val discussionsRepository: DiscussionsRepository
) {

    private val scope = MainScope()
    private val pagingConfig = PagingConfig(
        pageSize = 50,
        enablePlaceholders = false,
        initialLoadSize = 150,
        prefetchDistance = 150
    )

    fun getDiscussionPagingData(
        searchQuery: String? = null
    ): Flow<PagingData<DiscussionItemUI>> {
        return Pager(
            clientScope = scope,
            config = pagingConfig,
            initialKey = 0,
            getItems = { currentKey, size ->

                val items =
                    discussionsRepository.getDiscussions(
                        cursor = currentKey,
                        pageSize = size,
                        searchQuery = searchQuery
                    ).single().map { discussionsDomain ->
                        discussionsDomain.map { domain ->
                            domain.toDiscussionItemUI()
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
}