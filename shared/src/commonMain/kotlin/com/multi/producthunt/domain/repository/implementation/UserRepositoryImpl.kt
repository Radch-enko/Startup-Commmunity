package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.body.RegisterBody
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.RegisterResponse
import com.multi.producthunt.network.service.ProjectsApiService
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val service: ProjectsApiService) : UserRepository {

    override fun login(username: String, password: String): Flow<ApiResult<LoginResponse>> {
        return service.login(LoginBody(username, password))
    }

    override fun register(
        name: String,
        username: String,
        headline: String?,
        profileImage: String?,
        coverImage: String?,
        password: String,
        password2: String
    ): Flow<ApiResult<RegisterResponse>> {
        return service.register(
            RegisterBody(
                name,
                username,
                headline,
                profileImage,
                coverImage,
                password,
                password2
            )
        )
    }
}