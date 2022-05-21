package com.multi.producthunt.domain.repository

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(username: String, password: String): Flow<ApiResult<LoginResponse>>
}