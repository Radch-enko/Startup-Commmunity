package com.multi.producthunt.domain.repository

import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.model.StartupDomain
import com.multi.producthunt.network.util.CommonFlow

interface StartupsRepository {

    fun getStartupsPagingData(): CommonFlow<PagingData<StartupDomain>>
}