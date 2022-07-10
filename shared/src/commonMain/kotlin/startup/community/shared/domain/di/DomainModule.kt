package startup.community.shared.domain.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import startup.community.shared.domain.repository.*
import startup.community.shared.domain.repository.implementation.*
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.domain.usecase.GetDiscussionsUseCase
import startup.community.shared.domain.usecase.GetStartupsUseCase
import startup.community.shared.domain.usecase.GetUsersUseCase

val domainModule = DI.Module("Domain Module", false, "domain", fun DI.Builder.() {
    bindSingleton<StartupsRepository> { StartupsRepositoryImpl(instance()) }
    bindSingleton { GetStartupsUseCase(instance()) }
    bindSingleton<UserRepository> { UserRepositoryImpl(instance()) }
    bindSingleton<TopicsRepository> { TopicsRepositoryImpl(instance()) }
    bindSingleton { AuthorizationUseCase(instance()) }
    bindSingleton { GetUsersUseCase(instance()) }
    bindSingleton<DiscussionsRepository> { DiscussionRepositoryImpl(instance()) }
    bindSingleton { GetDiscussionsUseCase(instance()) }
    bindSingleton<ReportingRepository> { ReportingRepositoryImpl(instance()) }
})