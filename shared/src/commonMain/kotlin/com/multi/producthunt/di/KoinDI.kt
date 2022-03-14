package com.multi.producthunt.di

import com.multi.producthunt.domain.di.domainModule
import com.multi.producthunt.network.di.networkModule
import org.kodein.di.DI

val koin = DI {
    import(networkModule)
    import(domainModule)
}