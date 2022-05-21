package com.multi.producthunt.android.screen.login.di

import com.multi.producthunt.android.screen.login.LoginScreenViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val loginModule = DI.Module(name = "login") {
    bindProvider { LoginScreenViewModel(instance(), instance(), instance(), instance()) }
}