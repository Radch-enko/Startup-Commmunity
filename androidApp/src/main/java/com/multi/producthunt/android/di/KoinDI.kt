package com.multi.producthunt.android.di

import com.multi.producthunt.android.screen.home.di.homeModule
import com.multi.producthunt.android.screen.settings.di.settingsModule
import com.multi.producthunt.android.screen.timeline.di.timelineModule
import com.multi.producthunt.domain.di.domainModule
import com.multi.producthunt.network.di.networkModule
import org.kodein.di.DI

val koin = DI {
    import(networkModule)
    import(domainModule)
    import(homeModule)
    import(timelineModule)
    import(settingsModule)
}