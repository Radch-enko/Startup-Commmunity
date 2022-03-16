package com.multi.producthunt.android.di

import com.multi.producthunt.android.config.ProductHuntConfig
import com.multi.producthunt.android.screen.home.di.homeModule
import com.multi.producthunt.domain.di.domainModule
import com.multi.producthunt.network.di.networkModule
import com.multi.producthunt.utils.AppConfig
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val koin = DI {
    bindSingleton<AppConfig> { ProductHuntConfig() }
    import(networkModule)
    import(domainModule)
    import(homeModule)
}