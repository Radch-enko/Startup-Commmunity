package startup.community.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.UserDomain
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.response.LoginResponse
import startup.community.shared.network.model.response.UserResponse

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