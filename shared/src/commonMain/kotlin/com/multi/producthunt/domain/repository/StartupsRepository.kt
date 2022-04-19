package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.PostsDomain

interface StartupsRepository {
    suspend fun getTopStartups(after: String = ""): PostsDomain
}