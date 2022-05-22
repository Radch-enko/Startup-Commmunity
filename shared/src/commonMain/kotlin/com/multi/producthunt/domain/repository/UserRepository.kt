package com.multi.producthunt.domain.repository

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(username: String, password: String): Flow<ApiResult<LoginResponse>>
    fun register(
        name: String,
        username: String,
        headline: String? = null,
        profileImage: String?= null,
        coverImage: String?= null,
        password: String,
        password2: String,
    ): Flow<ApiResult<RegisterResponse>>
}