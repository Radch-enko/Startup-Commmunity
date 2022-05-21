package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.service.ProjectsApiService
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val service: ProjectsApiService) : UserRepository {

    override suspend fun login(username: String, password: String): Flow<ApiResult<LoginResponse>> {
        return service.login(LoginBody(username, password))
    }
}