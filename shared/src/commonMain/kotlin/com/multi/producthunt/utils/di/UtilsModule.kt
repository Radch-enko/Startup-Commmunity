package com.multi.producthunt.utils.di

import com.multi.producthunt.utils.AuthTokenManager
import com.multi.producthunt.utils.TokenManager
import com.multi.producthunt.utils.Validator
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val utilsModule = DI.Module {
    bindSingleton<TokenManager> { AuthTokenManager() }
    bindSingleton { Validator() }
}