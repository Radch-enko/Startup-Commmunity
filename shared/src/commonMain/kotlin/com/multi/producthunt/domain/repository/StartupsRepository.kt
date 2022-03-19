package com.multi.producthunt.domain.repository

import com.multi.producthunt.StartupsQuery
import com.multi.producthunt.domain.model.PostsDomain
import com.multi.producthunt.domain.model.StartupDomain

interface StartupsRepository {
    suspend fun getTopStartups(after: String = ""): PostsDomain
}