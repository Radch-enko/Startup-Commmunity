package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.UserDomain
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(username: String, password: String): Flow<ApiResult<LoginResponse>>
    fun register(
        name: String,
        username: String,
        headline: String?,
        password: String,
        password2: String,
    ): Flow<ApiResult<UserResponse>>

    fun me(): Flow<ApiResult<UserDomain>>

    fun updateUser(
        name: String? = null,
        headline: String? = null,
        profileImage: String? = null,
        coverImage: String? = null,
    ): Flow<ApiResult<UserDomain>>

    fun getUserById(id: Int): Flow<ApiResult<UserDomain>>

    fun getAllUsers(
        cursor: Int,
        pageSize: Int?, searchQuery: String
    ): Flow<ApiResult<List<UserDomain>>>
}