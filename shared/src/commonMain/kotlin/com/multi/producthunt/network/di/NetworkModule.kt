package com.multi.producthunt.network.di

import com.multi.producthunt.network.client.KtorfitClient
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val networkModule = DI.Module {
    bindSingleton { KtorfitClient(instance()).getService() }
}