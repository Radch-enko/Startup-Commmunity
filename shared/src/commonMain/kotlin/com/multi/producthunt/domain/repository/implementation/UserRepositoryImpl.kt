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
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

    override fun me(token: String?): Flow<ApiResult<UserDomain>> {
        return service.me("Bearer " + token.orEmpty()).toDomain()
    }

    override fun uploadProfileImage(
        token: String?,
        profileImage: ByteArray?
    ): Flow<ApiResult<UserDomain>> {
        val now: Instant = Clock.System.now()
        val thisTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        val fileName =
            "${thisTime.dayOfMonth}_${thisTime.month}_${thisTime.year}_${thisTime.nanosecond}"
        val multipart = formData {
            if (profileImage != null) {
                append("image", profileImage, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=$fileName")
                })
            }
        }
        return service.updateProfileImage(multipart, "Bearer " + token.orEmpty()).toDomain()
    }

    override fun uploadCoverImage(
        token: String?,
        coverImage: ByteArray?
    ): Flow<ApiResult<UserDomain>> {
        val now: Instant = Clock.System.now()
        val thisTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        val fileName =
            "${thisTime.dayOfMonth}_${thisTime.month}_${thisTime.year}_${thisTime.nanosecond}"
        val multipart = formData {
            if (coverImage != null) {
                append("image", coverImage, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=$fileName")
                })
            }
        }
        return service.updateCoverImage(multipart, "Bearer " + token.orEmpty()).toDomain()
    }

    override fun updateUser(
        token: String?,
        name: String?,
        headline: String?
    ): Flow<ApiResult<UserDomain>> {
        return service.updateUser(UpdateUserBody(name, headline), "Bearer " + token.orEmpty())
            .toDomain()
    }
}