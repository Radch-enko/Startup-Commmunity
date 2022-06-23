package com.multi.producthunt.domain.di

import com.multi.producthunt.domain.repository.*
import com.multi.producthunt.domain.repository.implementation.*
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.domain.usecase.GetDiscussionsUseCase
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.domain.usecase.GetUsersUseCase
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val domainModule = DI.Module {
    bindSingleton<StartupsRepository> { StartupsRepositoryImpl(instance()) }
    bindSingleton { GetStartupsUseCase(instance()) }
    bindSingleton<UserRepository> { UserRepositoryImpl(instance()) }
    bindSingleton<TopicsRepository> { TopicsRepositoryImpl(instance()) }
    bindSingleton<AuthorizationUseCase> { AuthorizationUseCase(instance()) }
    bindSingleton<GetUsersUseCase> { GetUsersUseCase(instance()) }
    bindSingleton<DiscussionsRepository> { DiscussionRepositoryImpl(instance()) }
    bindSingleton<GetDiscussionsUseCase> { GetDiscussionsUseCase(instance()) }
    bindSingleton<ReportingRepository> { ReportingRepositoryImpl(instance()) }
}