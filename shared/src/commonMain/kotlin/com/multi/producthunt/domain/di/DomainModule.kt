package com.multi.producthunt.domain.di

import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.domain.repository.implementation.StartupsRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val domainModule = DI.Module {
    bindSingleton<StartupsRepository> { StartupsRepositoryImpl(instance()) }
}