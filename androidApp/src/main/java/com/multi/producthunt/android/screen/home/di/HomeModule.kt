package com.multi.producthunt.android.screen.home.di

import com.multi.producthunt.android.screen.home.HomeScreenViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val homeModule = DI.Module(name = "home") {
    bindProvider { HomeScreenViewModel(instance(), instance()) }
}