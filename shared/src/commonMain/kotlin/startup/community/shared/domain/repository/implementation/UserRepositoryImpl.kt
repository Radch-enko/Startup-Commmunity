package startup.community.shared.domain.repository.implementation

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.UserDomain
import startup.community.shared.domain.model.toDomain
import startup.community.shared.domain.repository.UserRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.body.LoginBody
import startup.community.shared.network.model.body.RegisterBody
import startup.community.shared.network.model.body.UpdateUserBody
import startup.community.shared.network.model.response.LoginResponse
import startup.community.shared.network.model.response.UserResponse
import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.network.util.asCommonFlow

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

    override fun getAllUsers(
        cursor: Int,
        pageSize: Int?, searchQuery: String
    ): Flow<ApiResult<List<UserDomain>>> {
        return service.getAllUsers(cursor, pageSize ?: 10, searchQuery).asCommonFlow().toDomain()
    }
}