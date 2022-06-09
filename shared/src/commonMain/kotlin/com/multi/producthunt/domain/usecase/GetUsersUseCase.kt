package com.multi.producthunt.domain.usecase

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.util.asCommonFlow
import com.multi.producthunt.ui.models.SearchUserUI
import com.multi.producthunt.ui.models.toSearchUserUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class GetUsersUseCase(
    private val repository: UserRepository
) {

    private val scope = MainScope()
    private val pagingConfig = PagingConfig(
        pageSize = 50,
        enablePlaceholders = false,
        initialLoadSize = 150,
        prefetchDistance = 150
    )

    fun getUsersPagingData(
        searchQuery: String? = null
    ): Flow<PagingData<SearchUserUI>> {
        return Pager(
            clientScope = scope,
            config = pagingConfig,
            initialKey = 0,
            getItems = { currentKey, size ->

                val items =
                    repository.getAllUsers(
                        cursor = currentKey,
                        pageSize = size,
                        searchQuery = searchQuery.orEmpty()
                    )
                        .single().map { usersDomain ->
                            usersDomain.map { domain ->
                                domain.toSearchUserUI()
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