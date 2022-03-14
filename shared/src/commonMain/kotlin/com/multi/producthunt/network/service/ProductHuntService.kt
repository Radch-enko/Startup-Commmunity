package com.multi.producthunt.network.service

import com.multi.producthunt.StartupsQuery

interface ProductHuntService {
    suspend fun getStartups(after: String): StartupsQuery.Posts
}