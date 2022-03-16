package com.multi.producthunt.network.di

import com.multi.producthunt.network.client.apolloClient
import com.multi.producthunt.network.service.ProductHuntService
import com.multi.producthunt.network.service.ProductHuntServiceImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val networkModule = DI.Module {
    bindSingleton { apolloClient(instance()) }
    bindSingleton<ProductHuntService> { ProductHuntServiceImpl(instance()) }
}