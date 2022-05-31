package com.multi.producthunt.network.di

import com.multi.producthunt.network.client.KtorfitClient
import com.multi.producthunt.network.service.ProjectsApiService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val networkModule = DI.Module {
    bind<ProjectsApiService>() with factory { KtorfitClient(instance()).getService() }
}