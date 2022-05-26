package com.multi.producthunt.domain.di

import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.domain.repository.implementation.StartupsRepositoryImpl
import com.multi.producthunt.domain.repository.implementation.TopicsRepositoryImpl
import com.multi.producthunt.domain.repository.implementation.UserRepositoryImpl
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val domainModule = DI.Module {
    bindSingleton<StartupsRepository> { StartupsRepositoryImpl(instance(), instance(), instance()) }
    bindSingleton { GetStartupsUseCase(instance()) }
    bindSingleton<UserRepository> { UserRepositoryImpl(instance()) }
    bindSingleton<TopicsRepository> { TopicsRepositoryImpl(instance()) }
}