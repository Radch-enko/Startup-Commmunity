package com.multi.producthunt.android.screen.home

import cafe.adriel.voyager.core.model.ScreenModel
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.model.StartupDomain
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.util.CommonFlow

class HomeScreenViewModel(private val repository: StartupsRepository) : ScreenModel {

    fun getPagingData(): CommonFlow<PagingData<StartupDomain>> {
        return repository.getStartupsPagingData()
    }
}