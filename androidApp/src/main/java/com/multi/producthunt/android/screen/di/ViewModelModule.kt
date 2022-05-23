package com.multi.producthunt.android.screen.di

import com.multi.producthunt.android.screen.authorization.AuthorizationViewModel
import com.multi.producthunt.android.screen.home.HomeScreenViewModel
import com.multi.producthunt.android.screen.profile.ProfileScreenViewModel
import com.multi.producthunt.android.screen.settings.SettingsScreenViewModel
import com.multi.producthunt.android.screen.timeline.TimelineScreenViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val viewModelModule = DI.Module("viewModelModule") {
    bindProvider { HomeScreenViewModel(instance()) }
    bindProvider { TimelineScreenViewModel(instance()) }
    bindProvider { SettingsScreenViewModel(instance()) }
    bindProvider { AuthorizationViewModel(instance(), instance()) }
    bindProvider { ProfileScreenViewModel(instance(), instance()) }
}