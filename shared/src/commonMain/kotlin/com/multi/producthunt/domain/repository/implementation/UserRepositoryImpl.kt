package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.UserDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.LoginBody
import com.multi.producthunt.network.model.body.RegisterBody
import com.multi.producthunt.network.model.body.UpdateUserBody
import com.multi.producthunt.network.model.response.LoginResponse
import com.multi.producthunt.network.model.response.UserResponse
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
        password: String,
        password2: String
    ): Flow<ApiResult<UserResponse>> {
        return service.register(
            RegisterBody(
                name,
                username,
                headline,
                password,
                password2
            )
        )
    }

    override fun me(): Flow<ApiResult<UserDomain>> {
        return service.me().toDomain()
    }

    override fun updateUser(
        name: String?,
        headline: String?,
        profileImage: String?,
        coverImage: String?,
    ): Flow<ApiResult<UserDomain>> {
        return service.updateUser(
            UpdateUserBody(
                name, headline,
                profileImage, coverImage
            )
        )
            .toDomain()
    }

    override fun getUserById(id: Int): Flow<ApiResult<UserDomain>> {
        return service.getUserById(id).toDomain()
    }
}