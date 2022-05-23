package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDomain(
    val name: String,
    val username: String,
    val headline: String?,
    val profileImage: String?,
    val coverImage: String?,
)


fun Flow<ApiResult<UserResponse>>.toDomain(): Flow<ApiResult<UserDomain>> {
    return this.map {
        it.map { userResponse ->
            UserDomain(
                name = userResponse.name,
                username = userResponse.username,
                headline = userResponse.headline,
                profileImage = userResponse.profileImage,
                coverImage = userResponse.coverImage
            )
        }
    }
}